package com.gamestore.gameplaystore.Subcategoria;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gamestore.gameplaystore.Categoria.Categoria;
import com.gamestore.gameplaystore.Categoria.CategoriaRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SubcategoriaService {

    private final SubcategoriaRepository subcategoriaRepository;
    private final CategoriaRepository categoriaRepository;

    @SuppressWarnings("null")
    public Subcategoria create(SubcategoriaRequest request) {
        Categoria categoria = resolveCategoria(request.categoriaId());

        Subcategoria subcategoria = Subcategoria.builder()
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .activa(request.activa() == null || request.activa())
                .categoria(categoria)
                .build();

        Subcategoria creada = subcategoriaRepository.save(subcategoria);
        return creada;
    }

    @SuppressWarnings("null")
    public Subcategoria update(long id, SubcategoriaRequest request) {
        Subcategoria existente = findById(id);
        Categoria categoria = resolveCategoria(request.categoriaId());

        existente.setNombre(request.nombre());
        existente.setDescripcion(request.descripcion());
        existente.setActiva(request.activa() == null || request.activa());
        existente.setCategoria(categoria);

        Subcategoria actualizado = subcategoriaRepository.save(existente);
        return actualizado;
    }

    public void delete(long id) {
        Subcategoria existente = findById(id);
        subcategoriaRepository.delete(existente);
    }

    @Transactional(readOnly = true)
    public List<Subcategoria> findAll() {
        return subcategoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Subcategoria> findByCategoria(long categoriaId) {
        return subcategoriaRepository.findByCategoria_Id(categoriaId);
    }

    @Transactional(readOnly = true)
    public Subcategoria findById(long id) {
        return subcategoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subcategoria no encontrada: " + id));
    }

    private Categoria resolveCategoria(long categoriaId) {
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada: " + categoriaId));
    }
}


