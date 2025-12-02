package com.gamestore.gameplaystore.Subcategoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SubcategoriaRequest(
        @NotBlank @Size(max = 120) String nombre,
        @Size(max = 300) String descripcion,
        Boolean activa,
        @NotNull Long categoriaId) {
}


