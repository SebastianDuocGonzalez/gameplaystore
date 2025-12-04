package com.gamestore.gameplaystore.Ordenes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalles_orden")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleOrden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Guardamos el ID del producto por si el producto se borra en el futuro,
    // o podrías hacer una relación @ManyToOne con Producto si prefieres integridad estricta.
    private Long productoId; 
    
    private String nombreProducto; // Guardamos el nombre "congelado" al momento de la compra

    private Integer cantidad;

    private Double precioUnitario;

    private Double subtotal;

    @ManyToOne
    @JoinColumn(name = "orden_id")
    @JsonIgnore // Evita bucles infinitos al serializar a JSON
    private Orden orden;
}