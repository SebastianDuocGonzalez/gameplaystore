package com.gamestore.gameplaystore.Producto;

import java.math.BigDecimal;

import com.gamestore.gameplaystore.Categoria.CategoriaResponse;
import com.gamestore.gameplaystore.Subcategoria.SubcategoriaResponse;

public record ProductoResponse(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer stock,
        String tipo,
        CategoriaResponse categoria,
        SubcategoriaResponse subcategoria) {
}

