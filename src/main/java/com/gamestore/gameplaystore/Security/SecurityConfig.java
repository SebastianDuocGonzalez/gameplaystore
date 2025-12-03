package com.gamestore.gameplaystore.Security;

import com.gamestore.gameplaystore.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults()) // Activa la configuración de tu CorsConfig.java
            .csrf(csrf -> csrf.disable())    // Necesario para que funcionen los POST desde React
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())) // Para consola H2
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Backend sin memoria
            
            .authorizeHttpRequests(auth -> auth
                // 1. Rutas Públicas (Login, Registro y Acceso a imágenes/css si fuera necesario)
                .requestMatchers("/api/v1/auth/**").permitAll() 
                .requestMatchers("/h2-console/**").permitAll()
                
                // 2. Catálogo Público (Cualquiera puede VER productos)
                .requestMatchers(HttpMethod.GET, "/api/v1/productos/**").permitAll()

                // 3. Rutas de Administrador (Crear, Editar, Borrar)
                // Nota: hasRole("ADMIN") busca automáticamente "ROLE_ADMIN"
                .requestMatchers(HttpMethod.POST, "/api/v1/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/productos/**").hasRole("ADMIN")
                
                // Gestión de usuarios (Solo admin)
                .requestMatchers("/api/v1/users/**").hasRole("ADMIN")

                // 4. Cualquier otra petición requiere estar logueado
                .anyRequest().authenticated()
            )
            .userDetailsService(userService)
            .httpBasic(Customizer.withDefaults()); // Habilita autenticación básica

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}