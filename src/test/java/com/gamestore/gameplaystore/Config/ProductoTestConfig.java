package com.gamestore.gameplaystore.Config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class ProductoTestConfig {
    @Bean
    @Primary
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Permitir todo durante tests para evitar problemas de auth
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(requests -> requests.anyRequest().permitAll());
        return http.build();
    }

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        // Usa el delegating encoder que soporta múltiples algoritmos
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}