package com.gamestore.gameplaystore.Catalogo;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gamestore.gameplaystore.Categoria.Categoria;
import com.gamestore.gameplaystore.Categoria.CategoriaRepository;
import com.gamestore.gameplaystore.Categoria.CategoriaResponse;
import com.gamestore.gameplaystore.Producto.Producto;
import com.gamestore.gameplaystore.Producto.ProductoMapper;
import com.gamestore.gameplaystore.Producto.ProductoRepository;
import com.gamestore.gameplaystore.Producto.ProductoResponse;
import com.gamestore.gameplaystore.Subcategoria.Subcategoria;
import com.gamestore.gameplaystore.Subcategoria.SubcategoriaRepository;
import com.gamestore.gameplaystore.Subcategoria.SubcategoriaResponse;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CatalogoService {

    private final CategoriaRepository categoriaRepository;
    private final SubcategoriaRepository subcategoriaRepository;
    private final ProductoRepository productoRepository;

    public CatalogoResponse obtenerCatalogo(CanalVenta canal) {
        List<Categoria> categorias = categoriaRepository.findAll();
        Map<Long, List<Subcategoria>> subcategoriasPorCategoria = subcategoriaRepository.findAll().stream()
                .collect(Collectors.groupingBy(sub -> sub.getCategoria().getId()));
        List<Producto> productos = productoRepository.findAll();
        Map<Long, List<Producto>> productosPorCategoria = productos.stream()
                .collect(Collectors.groupingBy(prod -> prod.getCategoria().getId()));
        Map<Long, List<Producto>> productosPorSubcategoria = productos.stream()
                .filter(prod -> prod.getSubcategoria() != null)
                .collect(Collectors.groupingBy(prod -> prod.getSubcategoria().getId()));

        List<CategoriaCatalogoResponse> respuestaCategorias = categorias.stream()
                .map(categoria -> construirCategoriaCatalogo(
                        categoria,
                        subcategoriasPorCategoria.getOrDefault(categoria.getId(), List.of()),
                        productosPorCategoria.getOrDefault(categoria.getId(), List.of()),
                        productosPorSubcategoria,
                        canal))
                .toList();

        return new CatalogoResponse(canal, respuestaCategorias);
    }

    public CategoriaCatalogoResponse obtenerCatalogoPorCategoria(Long categoriaId, CanalVenta canal) {
        long categoriaIdValue = Objects.requireNonNull(categoriaId, "El identificador de la categoria es requerido");

        Categoria categoria = categoriaRepository.findById(categoriaIdValue)
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada: " + categoriaIdValue));

        List<Subcategoria> subcategorias = subcategoriaRepository.findByCategoria_Id(categoriaIdValue);
        List<Producto> productos = productoRepository.findByCategoria_Id(categoriaIdValue);
        Map<Long, List<Producto>> productosPorSubcategoria = productos.stream()
                .filter(prod -> prod.getSubcategoria() != null)
                .collect(Collectors.groupingBy(prod -> prod.getSubcategoria().getId()));

        return construirCategoriaCatalogo(
                categoria,
                subcategorias,
                productos,
                productosPorSubcategoria,
                canal);
    }

    private CategoriaCatalogoResponse construirCategoriaCatalogo(
            Categoria categoria,
            List<Subcategoria> subcategorias,
            List<Producto> productos,
            Map<Long, List<Producto>> productosPorSubcategoria,
            CanalVenta canal) {

        List<ProductoResponse> productosCategoria = aplicarReglasPorCanal(productos, canal).stream()
                .map(ProductoMapper::toResponse)
                .toList();

        List<SubcategoriaCatalogoResponse> respuestaSubcategorias = subcategorias.stream()
                .map(subcategoria -> new SubcategoriaCatalogoResponse(
                        SubcategoriaResponse.fromEntity(subcategoria),
                        aplicarReglasPorCanal(productosPorSubcategoria
                                .getOrDefault(subcategoria.getId(), List.of()), canal).stream()
                                .map(ProductoMapper::toResponse)
                                .toList()))
                .toList();

        return new CategoriaCatalogoResponse(
                CategoriaResponse.fromEntity(categoria),
                productosCategoria,
                respuestaSubcategorias);
    }

    private List<Producto> aplicarReglasPorCanal(List<Producto> productos, CanalVenta canal) {
        if (productos == null || productos.isEmpty()) {
            return List.of();
        }

        if (canal == CanalVenta.MOVIL) {
            return productos.stream()
                    .sorted(Comparator.comparing(Producto::getStock).reversed())
                    .limit(8)
                    .toList();
        }

        return productos;
    }
}


