package com.example.literalura.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String titulo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Autor autor;
    
    private String idioma;
    
    private Integer numeroDescargas;

    public Libro() {}

    public Libro(String titulo, Autor autor, String idioma, Integer numeroDescargas) {
        this.titulo = titulo;
        this.autor = autor;
        this.idioma = idioma;
        this.numeroDescargas = numeroDescargas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Integer numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    @Override
    public String toString() {
        String nombreAutor = "Desconocido";
        try {
            if (autor != null) {
                nombreAutor = autor.getNombre();
            }
        } catch (Exception e) {
            // En caso de error al acceder al autor, usar "Desconocido"
            nombreAutor = "Desconocido";
        }
        
        return String.format("""
                ----------- LIBRO -----------
                Título: %s
                Autor: %s
                Idioma: %s
                Número de descargas: %d
                ----------------------------""",
                titulo,
                nombreAutor,
                idioma,
                numeroDescargas != null ? numeroDescargas : 0);
    }
}