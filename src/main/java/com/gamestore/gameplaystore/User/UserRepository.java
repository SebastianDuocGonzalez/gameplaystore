package com.gamestore.gameplaystore.User;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Método de JPA para buscar por el campo 'email' heredado de Persona
    Optional<User> findByEmail(String email);
}
