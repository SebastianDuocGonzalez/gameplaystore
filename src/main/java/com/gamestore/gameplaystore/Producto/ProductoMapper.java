package com.gamestore.gameplaystore.Producto;

import java.util.List;

import com.gamestore.gameplaystore.Categoria.CategoriaResponse;
import com.gamestore.gameplaystore.Subcategoria.SubcategoriaResponse;

public final class ProductoMapper {

    private ProductoMapper() {
    }

    public static ProductoResponse toResponse(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getTipo(),
                CategoriaResponse.fromEntity(producto.getCategoria()),
                SubcategoriaResponse.fromEntity(producto.getSubcategoria()));
    }

    public static List<ProductoResponse> toResponseList(List<Producto> productos) {
        return productos.stream()
                .map(ProductoMapper::toResponse)
                .toList();
    }
}

