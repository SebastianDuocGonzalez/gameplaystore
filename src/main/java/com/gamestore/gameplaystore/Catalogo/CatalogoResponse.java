package com.gamestore.gameplaystore.Catalogo;

import java.util.List;

public record CatalogoResponse(
        CanalVenta canal,
        List<CategoriaCatalogoResponse> categorias) {
}


