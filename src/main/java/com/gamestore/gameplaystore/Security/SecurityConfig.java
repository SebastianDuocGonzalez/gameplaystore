package com.gamestore.gameplaystore.Security;

import com.gamestore.gameplaystore.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults()) // Activa la configuración de tu CorsConfig.java
            .csrf(csrf -> csrf.disable())    // Necesario para que funcionen los POST desde React
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())) // Para consola H2
            // Importante: Stateless para JWT
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            .authorizeHttpRequests(auth -> auth
                // 1. PÚBLICO (Login, Registro, Catálogo)
                .requestMatchers("/api/v1/auth/**").permitAll() // Login y Registro JWT
                .requestMatchers(HttpMethod.GET, "/api/v1/productos/**").permitAll() // Ver catálogo
                .requestMatchers("/h2-console/**").permitAll()
                
                // 2. ROL VENDEDOR (TRABAJADOR) Y ADMIN
                // Ambos pueden ver órdenes
                .requestMatchers(HttpMethod.GET, "/api/v1/ordenes").hasAnyRole("ADMIN", "TRABAJADOR")
                
                // 3. ROL ADMIN (Exclusivo)
                .requestMatchers(HttpMethod.POST, "/api/v1/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/productos/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/users/**").hasRole("ADMIN")// Gestión de usuarios

                // 4. ROL CLIENTE (y otros autenticados)
                .requestMatchers(HttpMethod.POST, "/api/v1/ordenes").authenticated() // Comprar

                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}