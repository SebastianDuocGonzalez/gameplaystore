package com.gamestore.gameplaystore.Catalogo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/catalogo")
@RequiredArgsConstructor
public class CatalogoController {

    private final CatalogoService catalogoService;

    @GetMapping("/web")
    public ResponseEntity<CatalogoResponse> obtenerCatalogoWeb() {
        return ResponseEntity.ok(catalogoService.obtenerCatalogo(CanalVenta.WEB));
    }

    @GetMapping("/movil")
    public ResponseEntity<CatalogoResponse> obtenerCatalogoMovil() {
        return ResponseEntity.ok(catalogoService.obtenerCatalogo(CanalVenta.MOVIL));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<CategoriaCatalogoResponse> obtenerCatalogoPorCategoria(
            @PathVariable Long categoriaId,
            @RequestParam(defaultValue = "WEB") CanalVenta canal) {
        return ResponseEntity.ok(catalogoService.obtenerCatalogoPorCategoria(categoriaId, canal));
    }
}


