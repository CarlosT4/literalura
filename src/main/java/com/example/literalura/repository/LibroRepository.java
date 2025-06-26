package com.example.literalura.repository;

import com.example.literalura.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    
    Optional<Libro> findByTituloContainingIgnoreCase(String titulo);
    
    List<Libro> findByIdioma(String idioma);
    
    @Query("SELECT l.idioma, COUNT(l) FROM Libro l GROUP BY l.idioma ORDER BY COUNT(l) DESC")
    List<Object[]> countLibrosPorIdioma();
    
    @Query("SELECT l FROM Libro l JOIN FETCH l.autor ORDER BY l.titulo")
    List<Libro> findAllOrderByTitulo();
}