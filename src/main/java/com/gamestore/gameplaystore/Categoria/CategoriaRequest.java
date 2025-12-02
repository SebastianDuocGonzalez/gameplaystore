package com.gamestore.gameplaystore.Categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(
        @NotBlank @Size(max = 100) String nombre,
        @Size(max = 300) String descripcion) {
}

