package com.example.literalura.service;

import com.example.literalura.dto.AutorDTO;
import com.example.literalura.dto.LibroDTO;
import com.example.literalura.dto.RespuestaApiDTO;
import com.example.literalura.entity.Autor;
import com.example.literalura.entity.Libro;
import com.example.literalura.repository.AutorRepository;
import com.example.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LibroService {
    
    private static final String URL_BASE = "https://gutendex.com/books/";
    
    @Autowired
    private ConsumoApi consumoApi;
    
    @Autowired
    private ConversorDatos conversor;
    
    @Autowired
    private LibroRepository libroRepository;
    
    @Autowired
    private AutorRepository autorRepository;
    
    @Transactional
    public void buscarLibroPorTitulo(String titulo) {
        String url = URL_BASE + "?search=" + titulo.replace(" ", "%20");
        
        try {
            System.out.println("Buscando libro: " + titulo);
            String json = consumoApi.obtenerDatos(url);
            
            if (json == null || json.trim().isEmpty()) {
                System.out.println("La API retornó una respuesta vacía");
                return;
            }
            
            System.out.println("Respuesta JSON recibida (primeros 200 caracteres): " + 
                              json.substring(0, Math.min(200, json.length())));
            
            RespuestaApiDTO respuesta = conversor.obtenerDatos(json, RespuestaApiDTO.class);
            
            if (respuesta.getResultados() != null && !respuesta.getResultados().isEmpty()) {
                LibroDTO libroDTO = respuesta.getResultados().get(0);
                
                // Verificar si el libro ya existe
                Optional<Libro> libroExistente = libroRepository.findByTituloContainingIgnoreCase(libroDTO.getTitulo());
                if (libroExistente.isPresent()) {
                    System.out.println("El libro ya está registrado en la base de datos:");
                    System.out.println(libroExistente.get());
                    return;
                }
                
                // Crear o buscar autor
                Autor autor = crearOBuscarAutor(libroDTO);
                
                // Crear libro con el autor gestionado por el contexto de persistencia
                Libro libro = new Libro();
                libro.setTitulo(libroDTO.getTitulo());
                libro.setAutor(autor);
                libro.setIdioma(libroDTO.getIdiomas() != null && !libroDTO.getIdiomas().isEmpty() 
                    ? libroDTO.getIdiomas().get(0) : "Desconocido");
                libro.setNumeroDescargas(libroDTO.getNumeroDescargas());
                
                libroRepository.save(libro);
                System.out.println("Libro guardado exitosamente:");
                System.out.println(libro);
                
            } else {
                System.out.println("No se encontraron libros con el título: " + titulo);
            }
            
        } catch (Exception e) {
            System.out.println("Error al buscar el libro: " + e.getMessage());
            e.printStackTrace(); // Para debug
        }
    }
    
    private Autor crearOBuscarAutor(LibroDTO libroDTO) {
        if (libroDTO.getAutores() != null && !libroDTO.getAutores().isEmpty()) {
            AutorDTO autorDTO = libroDTO.getAutores().get(0);
            
            // Buscar autor existente
            Optional<Autor> autorExistente = autorRepository.findByNombre(autorDTO.getNombre());
            if (autorExistente.isPresent()) {
                return autorExistente.get(); // Retornar la entidad gestionada
            }
            
            // Crear nuevo autor
            Autor nuevoAutor = new Autor();
            nuevoAutor.setNombre(autorDTO.getNombre());
            nuevoAutor.setFechaNacimiento(autorDTO.getFechaNacimiento());
            nuevoAutor.setFechaMuerte(autorDTO.getFechaMuerte());
            
            return autorRepository.save(nuevoAutor); // Guardar y retornar la entidad gestionada
        }
        
        // Manejar autor desconocido
        Optional<Autor> autorDesconocido = autorRepository.findByNombre("Desconocido");
        if (autorDesconocido.isPresent()) {
            return autorDesconocido.get();
        }
        
        Autor autorDesconocidoNuevo = new Autor("Desconocido", null, null);
        return autorRepository.save(autorDesconocidoNuevo);
    }
    
    @Transactional(readOnly = true)
    public void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAllOrderByTitulo();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
        } else {
            System.out.println("\n========== LIBROS REGISTRADOS ==========");
            libros.forEach(System.out::println);
        }
    }
    
    @Transactional(readOnly = true)
    public void listarAutores() {
        List<Autor> autores = autorRepository.findAllOrderByNombre();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados en la base de datos.");
        } else {
            System.out.println("\n========== AUTORES REGISTRADOS ==========");
            autores.forEach(System.out::println);
        }
    }
    
    @Transactional(readOnly = true)
    public void listarAutoresVivosEnAño(Integer año) {
        List<Autor> autoresVivos = autorRepository.findAutoresVivosEnAño(año);
        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + año);
        } else {
            System.out.println("\n========== AUTORES VIVOS EN " + año + " ==========");
            autoresVivos.forEach(System.out::println);
        }
    }
    
    @Transactional(readOnly = true)
    public void mostrarEstadisticasPorIdioma() {
        List<Object[]> estadisticas = libroRepository.countLibrosPorIdioma();
        if (estadisticas.isEmpty()) {
            System.out.println("No hay estadísticas disponibles.");
        } else {
            System.out.println("\n========== ESTADÍSTICAS POR IDIOMA ==========");
            estadisticas.forEach(stat -> {
                String idioma = (String) stat[0];
                Long cantidad = (Long) stat[1];
                System.out.printf("%s: %d libros%n", obtenerNombreIdioma(idioma), cantidad);
            });
        }
    }
    
    private String obtenerNombreIdioma(String codigo) {
        return switch (codigo.toLowerCase()) {
            case "en" -> "Inglés";
            case "es" -> "Español";
            case "fr" -> "Francés";
            case "de" -> "Alemán";
            case "it" -> "Italiano";
            case "pt" -> "Portugués";
            case "ru" -> "Ruso";
            case "zh" -> "Chino";
            case "ja" -> "Japonés";
            default -> codigo.toUpperCase();
        };
    }
}