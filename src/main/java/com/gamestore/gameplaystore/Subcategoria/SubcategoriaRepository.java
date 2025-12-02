package com.gamestore.gameplaystore.Subcategoria;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SubcategoriaRepository extends JpaRepository<Subcategoria, Long> {

    List<Subcategoria> findByCategoria_Id(Long categoriaId);

    boolean existsByIdAndCategoria_Id(Long subcategoriaId, Long categoriaId);
}


