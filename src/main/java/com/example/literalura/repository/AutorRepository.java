package com.example.literalura.repository;

import com.example.literalura.entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    
    Optional<Autor> findByNombre(String nombre);
    
    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :año AND (a.fechaMuerte IS NULL OR a.fechaMuerte >= :año)")
    List<Autor> findAutoresVivosEnAño(@Param("año") Integer año);
    
    @Query("SELECT a FROM Autor a ORDER BY a.nombre")
    List<Autor> findAllOrderByNombre();
}