package com.gamestore.gameplaystore.Subcategoria;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/subcategorias")
@Validated
@RequiredArgsConstructor
public class SubcategoriaController {

    private final SubcategoriaService subcategoriaService;

    @PostMapping
    public ResponseEntity<SubcategoriaResponse> create(@Valid @RequestBody SubcategoriaRequest request) {
        Subcategoria subcategoria = subcategoriaService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(SubcategoriaResponse.fromEntity(subcategoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubcategoriaResponse> update(@PathVariable Long id,
            @Valid @RequestBody SubcategoriaRequest request) {
        Subcategoria subcategoria = subcategoriaService.update(id, request);
        return ResponseEntity.ok(SubcategoriaResponse.fromEntity(subcategoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subcategoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SubcategoriaResponse>> findAll(
            @RequestParam(required = false) Long categoriaId) {
        List<Subcategoria> subcategorias = categoriaId == null
                ? subcategoriaService.findAll()
                : subcategoriaService.findByCategoria(categoriaId);

        List<SubcategoriaResponse> response = subcategorias.stream()
                .map(SubcategoriaResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubcategoriaResponse> findById(@PathVariable Long id) {
        Subcategoria subcategoria = subcategoriaService.findById(id);
        return ResponseEntity.ok(SubcategoriaResponse.fromEntity(subcategoria));
    }
}


