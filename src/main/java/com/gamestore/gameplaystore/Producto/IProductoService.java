package com.gamestore.gameplaystore.Producto;

import java.util.List;

public interface IProductoService {

    Producto create(ProductoRequest request);

    Producto update(Long id, ProductoRequest request);

    void delete(Long id);

    List<Producto> findAll();

    Producto findById(Long id);
}
