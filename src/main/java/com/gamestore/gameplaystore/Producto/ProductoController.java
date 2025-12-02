package com.gamestore.gameplaystore.Producto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/productos")
@Validated
@RequiredArgsConstructor
public class ProductoController {

    private final IProductoService productoService;

    @PostMapping
    public ResponseEntity<ProductoResponse> create(@Valid @RequestBody ProductoRequest request) {
        Producto producto = productoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductoMapper.toResponse(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponse> update(@PathVariable Long id, @Valid @RequestBody ProductoRequest request) {
        Producto producto = productoService.update(id, request);
        return ResponseEntity.ok(ProductoMapper.toResponse(producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductoResponse>> findAll() {
        List<Producto> productos = productoService.findAll();
        return ResponseEntity.ok(ProductoMapper.toResponseList(productos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponse> findById(@PathVariable Long id) {
        Producto producto = productoService.findById(id);
        return ResponseEntity.ok(ProductoMapper.toResponse(producto));
    }
}
