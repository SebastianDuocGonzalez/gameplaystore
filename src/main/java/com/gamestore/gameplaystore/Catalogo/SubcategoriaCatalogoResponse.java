package com.gamestore.gameplaystore.Catalogo;

import java.util.List;

import com.gamestore.gameplaystore.Producto.ProductoResponse;
import com.gamestore.gameplaystore.Subcategoria.SubcategoriaResponse;

public record SubcategoriaCatalogoResponse(
        SubcategoriaResponse subcategoria,
        List<ProductoResponse> productos) {
}


