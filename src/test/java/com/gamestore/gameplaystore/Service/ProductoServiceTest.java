package com.gamestore.gameplaystore.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.gamestore.gameplaystore.Categoria.Categoria;
import com.gamestore.gameplaystore.Categoria.CategoriaRepository;
import com.gamestore.gameplaystore.Producto.Producto;
import com.gamestore.gameplaystore.Producto.ProductoRequest;
import com.gamestore.gameplaystore.Producto.ProductoService;
import com.gamestore.gameplaystore.Subcategoria.Subcategoria;
import com.gamestore.gameplaystore.Subcategoria.SubcategoriaRepository;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
@ActiveProfiles("test")
@Transactional // Revierte los cambios en BDD al terminar cada test
class ProductoServiceTest {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private SubcategoriaRepository subcategoriaRepository;

    private Categoria categoria;
    private Subcategoria subcategoria;

    @BeforeEach
    void setUp() {
        // Limpiamos datos previos para evitar conflictos de ID
        subcategoriaRepository.deleteAll();
        categoriaRepository.deleteAll();

        // Creamos datos reales en H2
        categoria = categoriaRepository.save(
                Categoria.builder()
                        .nombre("Servicios")
                        .descripcion("Categoria para pruebas de servicio")
                        .build());

        subcategoria = subcategoriaRepository.save(
                Subcategoria.builder()
                        .nombre("Complementos Digitales")
                        .descripcion("Subcategoria de prueba")
                        .categoria(categoria)
                        .activa(true) // IMPORTANTE: Asegúrate de que esté activa para que no falle tu validación
                        .build());
}

    @Test
    void createShouldPersistProducto() {
        ProductoRequest request = new ProductoRequest(
                "Mando Retro",
                "Control estilo clásico",
                BigDecimal.valueOf(49.90),
                8,
                "ACCESORIO",
                categoria.getId(),
                null);

        Producto producto = productoService.create(request);

        // Verificaciones
        assertThat(producto).isNotNull(); // Verificamos que no sea null primero
        assertThat(producto.getId()).isNotNull(); // Verificamos que se generó ID
        assertThat(producto.getCategoria().getId()).isEqualTo(categoria.getId());
    }

    @Test
    void updateShouldChangeAttributes() {
        // Primero creamos uno real
        ProductoRequest reqCrear = new ProductoRequest(
                "Base", "Base desc", BigDecimal.valueOf(20), 5, "GENERICA", categoria.getId(), null);
        Producto creado = productoService.create(reqCrear);

        // Intentamos actualizarlo
        Producto actualizado = productoService.update(creado.getId(), new ProductoRequest(
                "Base Pro",
                "Actualizado",
                BigDecimal.valueOf(30),
                10,
                "GENERICA",
                categoria.getId(),
                null));

        assertThat(actualizado.getNombre()).isEqualTo("Base Pro");
        assertThat(actualizado.getPrecio()).isEqualByComparingTo(BigDecimal.valueOf(30));
    }

    @Test
    void deleteShouldRemoveProducto() {
        ProductoRequest req = new ProductoRequest(
                "Eliminar", "Desc", BigDecimal.valueOf(15), 3, "GENERICA", categoria.getId(), null);
        Producto producto = productoService.create(req);
        Long id = producto.getId();

        productoService.delete(id);

        assertThatThrownBy(() -> productoService.findById(id))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void createShouldFailWhenCategoriaNotFound() {
        ProductoRequest request = new ProductoRequest(
                "Fallo", "Sin categoria", BigDecimal.TEN, 1, "OTRA", 9999L, null);

        assertThatThrownBy(() -> productoService.create(request))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void createShouldAssignSubcategoriaWhenValid() {
        ProductoRequest request = new ProductoRequest(
                "Bundle digital",
                "Incluye DLCs",
                BigDecimal.valueOf(59.90),
                12,
                "DIGITAL",
                categoria.getId(),
                subcategoria.getId());

        Producto producto = productoService.create(request);

        assertThat(producto.getSubcategoria()).isNotNull();
        assertThat(producto.getSubcategoria().getId()).isEqualTo(subcategoria.getId());
    }

    @Test
    @Transactional
    void createShouldFailWhenSubcategoriaNotInCategoria() {
        // Crear una categoría ajena
        Categoria otraCategoria = categoriaRepository.save(
                Categoria.builder()
                        .nombre("Otra Categoria")
                        .descripcion("Categoria ajena")
                        .build());

        // Crear subcategoría ligada a esa OTRA categoría
        Subcategoria subcategoriaInvalida = subcategoriaRepository.save(
                Subcategoria.builder()
                        .nombre("Sub no valida")
                        .categoria(otraCategoria) // <--- Pertenece a otra
                        .activa(true)
                        .build());

        // Intentamos crear producto en la Categoria ORIGINAL usando la Subcategoría AJENA
        ProductoRequest request = new ProductoRequest(
                "Producto Invalido",
                "Subcategoria incorrecta",
                BigDecimal.valueOf(10),
                2,
                "ERROR",
                categoria.getId(), // ID de la categoría original
                subcategoriaInvalida.getId());

        assertThatThrownBy(() -> productoService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}