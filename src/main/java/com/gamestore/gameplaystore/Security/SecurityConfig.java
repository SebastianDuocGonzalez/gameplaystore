package com.gamestore.gameplaystore.Security;

import com.gamestore.gameplaystore.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.*;
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
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            //Permite que H2 pueda acceder a su consola web
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            .authorizeHttpRequests(auth -> auth
                // 1. Rutas Públicas (IMPORTANTE: Agregar /api/v1/auth/**)
                .requestMatchers("/public/**").permitAll()
                 // <--- AGREGAR ESTO (Login y Register)
                                .requestMatchers(HttpMethod.GET, "/api/v1/productos/**").permitAll()
                // 2. Rutas de Admin
                .requestMatchers(HttpMethod.POST, "/api/v1/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/productos/**").hasRole("ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/users/**").hasRole("ADMIN") // Gestión de usuarios

                // 3. Resto autenticado
                .anyRequest().authenticated()
            )
            .userDetailsService(userService)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Corrección importante: 
        // Usamos el DelegatingPasswordEncoder para que entienda el prefijo {bcrypt} de tu data.sql
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}