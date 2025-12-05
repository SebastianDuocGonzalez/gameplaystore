package com.gamestore.gameplaystore.User;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    // --- LOGIN & REGISTER ---
    @GetMapping("/auth/me")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(mapUserToDto(user));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, String> request) {
        if (userRepository.findByEmail(request.get("email")).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "Email ya existe"));
        }

        User newUser = User.builder()
                .nombre(request.get("nombre"))
                .email(request.get("email"))
                .password(passwordEncoder.encode(request.get("password")))
                .rol(Rol.CLIENTE) // <--- AQUÍ ESTÁ EL CANDADO: Siempre se crea como CLIENTE
                .build();

        userRepository.save(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapUserToDto(newUser));
    }

    // --- GESTIÓN DE USUARIOS (SOLO ADMIN) ---
    
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        // Ordenamos por ID para que la lista no salte al editar
        users.sort((u1, u2) -> u1.getId().compareTo(u2.getId()));
        
        List<Map<String, Object>> dtos = users.stream().map(this::mapUserToDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // NUEVO: Endpoint para cambiar el rol
    @PutMapping("/users/{id}/rol")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> changeUserRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        try {
            String nuevoRolStr = body.get("rol");
            Rol nuevoRol = Rol.valueOf(nuevoRolStr); // Convierte String a Enum (TRABAJADOR, ADMIN, CLIENTE)
            
            user.setRol(nuevoRol);
            userRepository.save(user);
            
            return ResponseEntity.ok(mapUserToDto(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Rol inválido"));
        }
    }

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