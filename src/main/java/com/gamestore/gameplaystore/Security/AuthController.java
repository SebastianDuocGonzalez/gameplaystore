package com.gamestore.gameplaystore.Security;

import com.gamestore.gameplaystore.Rol.Rol;
import com.gamestore.gameplaystore.User.User;
import com.gamestore.gameplaystore.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

// NUEVO CONTROLADOR PARA LOGIN/REGISTRO CON JWT
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager; // Se inyecta desde SecurityConfig

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> request) {
        var user = User.builder()
                .nombre(request.get("nombre"))
                .email(request.get("email"))
                .password(passwordEncoder.encode(request.get("password")))
                .rol(Rol.CLIENTE) // Por defecto
                .build();
        
        userRepository.save(user);
        String token = jwtService.generateToken(user);
        
        return ResponseEntity.ok(Map.of("token", token, "rol", user.getRol().name()));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> request) {
        // Autentica usando Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.get("email"),
                        request.get("password")
                )
        );

        // Si llega aquí, las credenciales son correctas
        var user = userRepository.findByEmail(request.get("email")).orElseThrow();
        String token = jwtService.generateToken(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("rol", user.getRol().name());
        response.put("nombre", user.getNombre());
        response.put("email", user.getEmail());

        return ResponseEntity.ok(response);
    }
}