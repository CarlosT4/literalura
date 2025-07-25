package com.example.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LibroDTO {
    @JsonAlias("id")
    private Long id;
    
    @JsonAlias("title")
    private String titulo;
    
    @JsonAlias("authors")
    private List<AutorDTO> autores;
    
    @JsonAlias("languages")
    private List<String> idiomas;
    
    @JsonAlias("download_count")
    private Integer numeroDescargas;

    public LibroDTO() {}

    public LibroDTO(Long id, String titulo, List<AutorDTO> autores, List<String> idiomas, Integer numeroDescargas) {
        this.id = id;
        this.titulo = titulo;
        this.autores = autores;
        this.idiomas = idiomas;
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

    public List<AutorDTO> getAutores() {
        return autores;
    }

    public void setAutores(List<AutorDTO> autores) {
        this.autores = autores;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public Integer getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Integer numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    @Override
    public String toString() {
        return "LibroDTO{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", autores=" + autores +
                ", idiomas=" + idiomas +
                ", numeroDescargas=" + numeroDescargas +
                '}';
    }
}