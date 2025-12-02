package com.gamestore.gameplaystore.Producto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductoRequest(
        @NotBlank @Size(max = 150) String nombre,
        @Size(max = 500) String descripcion,
        @NotNull BigDecimal precio,
        @NotNull @Min(0) Integer stock,
        @Size(max = 60) String tipo,
        @NotNull Long categoriaId,
        Long subcategoriaId) {
}

