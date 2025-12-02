package com.gamestore.gameplaystore.Producto;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gamestore.gameplaystore.Categoria.Categoria;
import com.gamestore.gameplaystore.Categoria.CategoriaRepository;
import com.gamestore.gameplaystore.Subcategoria.Subcategoria;
import com.gamestore.gameplaystore.Subcategoria.SubcategoriaRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoService implements IProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final SubcategoriaRepository subcategoriaRepository;

    @Override
    public Producto create(ProductoRequest request) {
        Categoria categoria = resolveCategoria(request.categoriaId());
        Subcategoria subcategoria = resolveSubcategoria(request.subcategoriaId(), categoria);

        Producto producto = Producto.builder()
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .precio(request.precio())
                .stock(request.stock())
                .tipo(request.tipo())
                .categoria(categoria)
                .subcategoria(subcategoria)
                .build();

        return productoRepository.save(producto);
    }

    @Override
    public Producto update(Long id, ProductoRequest request) {
        Producto existente = findById(id);
        Categoria categoria = resolveCategoria(request.categoriaId());
        Subcategoria subcategoria = resolveSubcategoria(request.subcategoriaId(), categoria);

        existente.setNombre(request.nombre());
        existente.setDescripcion(request.descripcion());
        existente.setPrecio(request.precio());
        existente.setStock(request.stock());
        existente.setTipo(request.tipo());
        existente.setCategoria(categoria);
        existente.setSubcategoria(subcategoria);

        return productoRepository.save(existente);
    }

    @Override
    public void delete(Long id) {
        Producto existente = findById(id);
        productoRepository.delete(existente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Producto findById(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + id));
    }

    private Categoria resolveCategoria(Long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada: " + categoriaId));
    }

    private Subcategoria resolveSubcategoria(Long subcategoriaId, Categoria categoria) {
        if (subcategoriaId == null) {
            return null;
        }

        Subcategoria subcategoria = subcategoriaRepository.findById(subcategoriaId)
                .orElseThrow(() -> new EntityNotFoundException("Subcategoria no encontrada: " + subcategoriaId));

        if (!subcategoria.getCategoria().getId().equals(categoria.getId())) {
            throw new IllegalArgumentException("La subcategoria seleccionada no pertenece a la categoria indicada.");
        }

        if (!subcategoria.isActiva()) {
            throw new IllegalStateException("La subcategoria seleccionada se encuentra inactiva.");
        }

        return subcategoria;
    }
}
