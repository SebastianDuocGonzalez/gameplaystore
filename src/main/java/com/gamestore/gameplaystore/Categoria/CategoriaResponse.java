package com.gamestore.gameplaystore.Categoria;

public record CategoriaResponse(Long id, String nombre, String descripcion) {

    public static CategoriaResponse fromEntity(Categoria categoria) {
        if (categoria == null) {
            return null;
        }
        return new CategoriaResponse(categoria.getId(), categoria.getNombre(), categoria.getDescripcion());
    }
}

