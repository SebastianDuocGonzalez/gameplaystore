package com.gamestore.gameplaystore.Producto;

import com.gamestore.gameplaystore.Categoria.Categoria;
import com.gamestore.gameplaystore.Categoria.CategoriaRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    // --- DTO INTERNO PARA RECIBIR DATOS LIMPIOS ---
    @Data
    public static class ProductoRequest {
        private String nombre;
        private String descripcion;
        private BigDecimal precio;
        private Integer stock;
        private String tipo;
        private String imagen;
        private Long categoriaId; // Recibimos solo el ID, no el objeto completo
    }

    // 1. Obtener todos (Público)
    @GetMapping
    public ResponseEntity<List<Producto>> getAll() {
        return ResponseEntity.ok(productoRepository.findAll());
    }

    // 2. Obtener por ID (Público)
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getOne(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Crear Producto (ADMIN)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@RequestBody ProductoRequest request) {
        // Validamos que la categoría exista
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada (ID: " + request.getCategoriaId() + ")"));

        Producto producto = Producto.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precio(request.getPrecio())
                .stock(request.getStock())
                .tipo(request.getTipo())
                .imagen(request.getImagen())
                .categoria(categoria) // Asignamos la relación manualmente
                .build();

        return ResponseEntity.ok(productoRepository.save(producto));
    }

    // 4. Actualizar (ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ProductoRequest request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setTipo(request.getTipo());
        producto.setImagen(request.getImagen());
        producto.setCategoria(categoria);

        return ResponseEntity.ok(productoRepository.save(producto));
    }

    // 5. Eliminar (ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
