package com.example.literalura;

import com.example.literalura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.InputMismatchException;
import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    @Autowired
    private LibroService libroService;

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int opcion = -1;

        while (opcion != 0) {
            mostrarMenu();
            
            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer
                
                switch (opcion) {
                    case 1 -> {
                        System.out.print("Ingrese el nombre del libro que desea buscar: ");
                        String titulo = scanner.nextLine();
                        if (!titulo.trim().isEmpty()) {
                            libroService.buscarLibroPorTitulo(titulo.trim());
                        } else {
                            System.out.println("Debe ingresar un título válido.");
                        }
                    }
                    case 2 -> libroService.listarLibrosRegistrados();
                    case 3 -> libroService.listarAutores();
                    case 4 -> {
                        System.out.print("Ingrese el año que desea buscar: ");
                        try {
                            int año = scanner.nextInt();
                            scanner.nextLine(); // Limpiar buffer
                            if (año > 0) {
                                libroService.listarAutoresVivosEnAño(año);
                            } else {
                                System.out.println("Debe ingresar un año válido (mayor a 0).");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Debe ingresar un número válido.");
                            scanner.nextLine(); // Limpiar buffer
                        }
                    }
                    case 5 -> libroService.mostrarEstadisticasPorIdioma();
                    case 0 -> System.out.println("¡Gracias por usar Literalura! Hasta luego.");
                    default -> System.out.println("Opción no válida. Por favor, seleccione una opción del 0 al 5.");
                }
                
                if (opcion != 0) {
                    System.out.println("\nPresione Enter para continuar...");
                    scanner.nextLine();
                }
                
            } catch (InputMismatchException e) {
                System.out.println("Por favor, ingrese un número válido.");
                scanner.nextLine(); // Limpiar buffer
            } catch (Exception e) {
                System.out.println("Ocurrió un error: " + e.getMessage());
                scanner.nextLine(); // Limpiar buffer
            }
        }
        
        scanner.close();
    }

    private void mostrarMenu() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("🔶 LITERALURA - CATÁLOGO DE LIBROS 🔶");
        System.out.println("=".repeat(50));
        System.out.println("1 - Buscar libro por título");
        System.out.println("2 - Listar libros registrados");
        System.out.println("3 - Listar autores");
        System.out.println("4 - Listar autores vivos en un determinado año");
        System.out.println("5 - Listar libros por idioma");
        System.out.println("0 - Salir");
        System.out.println("=".repeat(50));
        System.out.print("Seleccione una opción: ");
    }
}