package com.gamestore.gameplaystore.Subcategoria;

public record SubcategoriaResponse(
        Long id,
        String nombre,
        String descripcion,
        boolean activa,
        Long categoriaId) {

    public static SubcategoriaResponse fromEntity(Subcategoria subcategoria) {
        if (subcategoria == null) {
            return null;
        }
        return new SubcategoriaResponse(
                subcategoria.getId(),
                subcategoria.getNombre(),
                subcategoria.getDescripcion(),
                subcategoria.isActiva(),
                subcategoria.getCategoria() != null ? subcategoria.getCategoria().getId() : null);
    }
}


