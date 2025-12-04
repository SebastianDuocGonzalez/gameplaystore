package com.gamestore.gameplaystore.User;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users") // Base path exclusivo para usuarios
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    // Obtener todos los usuarios
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<Map<String, Object>> dtos = users.stream()
                .map(this::mapUserToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Helper para no devolver la contraseña en el JSON
    private Map<String, Object> mapUserToDto(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("email", user.getEmail());
        map.put("nombre", user.getNombre());
        map.put("rol", user.getRol());
        return map;
    }
}