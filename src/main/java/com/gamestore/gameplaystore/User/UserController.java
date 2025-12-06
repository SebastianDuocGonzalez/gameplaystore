package com.gamestore.gameplaystore.User;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.gamestore.gameplaystore.Rol.Rol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;


    // --- GESTIÓN DE USUARIOS (SOLO ADMIN) ---
    
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TRABAJADOR')")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        // Ordenamos por ID para que la lista no salte al editar
        users.sort((u1, u2) -> u1.getId().compareTo(u2.getId()));
        
        List<Map<String, Object>> dtos = users.stream().map(this::mapUserToDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Endpoint para cambiar el rol
    @PutMapping("/users/{id}/rol")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> changeUserRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        try {
            String nuevoRolStr = body.get("rol");
            Rol nuevoRol = Rol.valueOf(nuevoRolStr);
            user.setRol(nuevoRol);
            userRepository.save(user);
            return ResponseEntity.ok(mapUserToDto(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Rol inválido"));
        }
    }

    // Endpoint para eliminar un usuario
    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Map<String, Object> mapUserToDto(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("email", user.getEmail());
        map.put("nombre", user.getNombre());
        map.put("rol", user.getRol());
        return map;
    }
}