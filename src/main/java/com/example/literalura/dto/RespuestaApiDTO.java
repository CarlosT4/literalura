package com.example.literalura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RespuestaApiDTO {
    @JsonAlias("count")
    private Integer count;
    
    @JsonAlias("next")
    private String next;
    
    @JsonAlias("previous")
    private String previous;
    
    @JsonAlias("results")
    private List<LibroDTO> resultados;

    public RespuestaApiDTO() {}

    public RespuestaApiDTO(Integer count, String next, String previous, List<LibroDTO> resultados) {
        this.count = count;
        this.next = next;
        this.previous = previous;
        this.resultados = resultados;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<LibroDTO> getResultados() {
        return resultados;
    }

    public void setResultados(List<LibroDTO> resultados) {
        this.resultados = resultados;
    }

    @Override
    public String toString() {
        return "RespuestaApiDTO{" +
                "count=" + count +
                ", next='" + next + '\'' +
                ", previous='" + previous + '\'' +
                ", resultados=" + resultados +
                '}';
    }
}