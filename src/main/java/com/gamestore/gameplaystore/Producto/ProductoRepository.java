package com.gamestore.gameplaystore.Producto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoria_Id(Long categoriaId);

    List<Producto> findByCategoria_NombreIgnoreCase(String categoriaNombre);

    List<Producto> findBySubcategoria_Id(Long subcategoriaId);

    List<Producto> findByCategoria_NombreIgnoreCaseAndSubcategoria_NombreIgnoreCase(String categoriaNombre,
            String subcategoriaNombre);
}
