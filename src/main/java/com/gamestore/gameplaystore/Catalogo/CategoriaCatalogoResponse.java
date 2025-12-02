package com.gamestore.gameplaystore.Catalogo;

import java.util.List;

import com.gamestore.gameplaystore.Categoria.CategoriaResponse;
import com.gamestore.gameplaystore.Producto.ProductoResponse;

public record CategoriaCatalogoResponse(
        CategoriaResponse categoria,
        List<ProductoResponse> productos,
        List<SubcategoriaCatalogoResponse> subcategorias) {
}


