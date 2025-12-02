package com.gamestore.gameplaystore.User;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder; // Importante
import org.springframework.web.bind.annotation.*;

import com.gamestore.gameplaystore.Rol.Rol; // Asegúrate de importar tu Enum Rol

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Necesario para encriptar al registrar

    // --- ENDPOINT DE LOGIN (Para auth.service.js) ---
    @GetMapping("/auth/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(mapUserToDto(user));
    }

    // --- ENDPOINT DE REGISTRO ---
    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, String> request) {
        // 1. Validar si el email ya existe
        if (userRepository.findByEmail(request.get("email")).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Email ya existe"));
        }

        // 2. Crear el nuevo usuario
        // Nota: Si usas BCrypt en prod, quita el prefijo "{noop}". 
        // Si usas la configuración flexible que vimos antes, el encoder lo manejará.
        String encodedPassword = passwordEncoder.encode(request.get("password"));

        User newUser = User.builder()
                .nombre(request.get("nombre"))
                .email(request.get("email"))
                .password(encodedPassword) // Guardamos la contraseña ya encriptada
                .rol(Rol.CLIENTE) // Por defecto, todos son clientes al registrarse
                .build();

        userRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapUserToDto(newUser));
    }

    // --- ENDPOINTS DE GESTIÓN (Para AdminUsers.js) ---
    @GetMapping("/users") // GET /api/v1/users
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<Map<String, Object>> dtos = users.stream().map(this::mapUserToDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/users/{id}") // DELETE /api/v1/users/{id}
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