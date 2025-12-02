package com.gamestore.gameplaystore.Categoria;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public Categoria create(CategoriaRequest request) {
        Categoria categoria = Categoria.builder()
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .build();

        return categoriaRepository.save(categoria);
    }

    public Categoria update(Long id, CategoriaRequest request) {
        Categoria existente = findById(id);
        existente.setNombre(request.nombre());
        existente.setDescripcion(request.descripcion());
        return categoriaRepository.save(existente);
    }

    public void delete(Long id) {
        Categoria existente = findById(id);
        categoriaRepository.delete(existente);
    }

    @Transactional(readOnly = true)
    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Categoria findById(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada: " + id));
    }
}

