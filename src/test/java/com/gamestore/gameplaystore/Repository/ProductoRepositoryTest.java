package com.gamestore.gameplaystore.Repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.gamestore.gameplaystore.Categoria.Categoria;
import com.gamestore.gameplaystore.Categoria.CategoriaRepository;
import com.gamestore.gameplaystore.Producto.Producto;
import com.gamestore.gameplaystore.Producto.ProductoRepository;
import com.gamestore.gameplaystore.Subcategoria.Subcategoria;
import com.gamestore.gameplaystore.Subcategoria.SubcategoriaRepository;

@SpringBootTest
@Transactional
class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    @Test
    void shouldPersistProductoWithCategoria() {
        Categoria categoria = categoriaRepository.save(
                Categoria.builder()
                        .nombre("Pruebas")
                        .descripcion("Categoria temporal")
                        .build());

        Subcategoria subcategoria = subcategoriaRepository.save(
                Subcategoria.builder()
                        .nombre("Perifericos Pro")
                        .categoria(categoria)
                        .build());

        Producto producto = productoRepository.save(
                Producto.builder()
                        .nombre("Control Pro")
                        .descripcion("Control inalambrico")
                        .precio(BigDecimal.valueOf(79.99))
                        .stock(12)
                        .tipo("ACCESORIO")
                        .categoria(categoria)
                        .subcategoria(subcategoria)
                        .build());

        assertThat(producto.getId()).isNotNull();
        assertThat(productoRepository.findById(producto.getId()))
                .isPresent()
                .get()
                .satisfies(productoPersistido -> {
                    assertThat(productoPersistido.getCategoria()).isEqualTo(categoria);
                    assertThat(productoPersistido.getSubcategoria()).isEqualTo(subcategoria);
                });
    }
}

