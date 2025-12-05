package com.gamestore.gameplaystore.Admin;

import com.gamestore.gameplaystore.Ordenes.OrdenRepository;
import com.gamestore.gameplaystore.Producto.ProductoRepository;
import com.gamestore.gameplaystore.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class DashboardController {

    private final UserRepository userRepository;
    private final ProductoRepository productoRepository;
    private final OrdenRepository ordenRepository;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRABAJADOR')")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        // Contamos datos reales de la BD
        long totalUsuarios = userRepository.count();
        long totalProductos = productoRepository.count();
        long totalOrdenes = ordenRepository.count();
        
        // Sumamos el dinero (Si es null, devolvemos 0)
        Double totalVentas = ordenRepository.sumTotalVentas();
        if (totalVentas == null) totalVentas = 0.0;

        stats.put("totalUsuarios", totalUsuarios);
        stats.put("totalProductos", totalProductos);
        stats.put("totalOrdenes", totalOrdenes);
        stats.put("totalVentas", totalVentas);

        return ResponseEntity.ok(stats);
    }
}