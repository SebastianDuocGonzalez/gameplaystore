package com.gamestore.gameplaystore.Ordenes;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
    // Para que el cliente vea SUS órdenes
    List<Orden> findByUserEmail(String email);
}