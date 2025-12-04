package com.gamestore.gameplaystore.Ordenes;

import com.gamestore.gameplaystore.User.User;
import com.gamestore.gameplaystore.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/ordenes")
@RequiredArgsConstructor
public class OrdenController {

    private final OrdenRepository ordenRepository;
    private final UserRepository userRepository;

    // 1. ADMIN y TRABAJADOR pueden ver TODAS las órdenes
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TRABAJADOR')")
    public ResponseEntity<List<Orden>> getAllOrdenes() {
        return ResponseEntity.ok(ordenRepository.findAll());
    }

    // 2. CLIENTE puede ver SOLO SUS órdenes
    @GetMapping("/mis-ordenes")
    public ResponseEntity<List<Orden>> getMisOrdenes() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(ordenRepository.findByUserEmail(email));
    }

    // 3. Crear Orden (Cualquier usuario autenticado)
    // Este endpoint recibe un JSON simple con la lista de productos
    @PostMapping
    public ResponseEntity<Orden> crearOrden(@RequestBody List<Map<String, Object>> items) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Orden orden = new Orden();
        orden.setFecha(LocalDateTime.now());
        orden.setUser(user);

        // Convertimos el JSON de entrada a entidades DetalleOrden
        // Se asume que el JSON trae: { "productoId": 1, "nombre": "Juego X", "precio": 1000, "cantidad": 2 }
        List<DetalleOrden> detalles = items.stream().map(item -> {
            return DetalleOrden.builder()
                    .productoId(Long.valueOf(item.get("id").toString()))
                    .nombreProducto(item.get("nombre").toString())
                    .precioUnitario(Double.valueOf(item.get("precio").toString()))
                    .cantidad(Integer.valueOf(item.get("cantidad").toString()))
                    .subtotal(Double.valueOf(item.get("precio").toString()) * Integer.valueOf(item.get("cantidad").toString()))
                    .orden(orden)
                    .build();
        }).collect(Collectors.toList());

        orden.setDetalles(detalles);
        orden.setTotal(detalles.stream().mapToDouble(DetalleOrden::getSubtotal).sum());

        ordenRepository.save(orden);
        return ResponseEntity.ok(orden);
    }
}