package com.gamestore.gameplaystore.Foro;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "eventos")
@Data
public class Evento {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String lugar;
    private LocalDateTime fechaEvento;
    private String juegoRelacionado;
    private String premio;
    @Column(columnDefinition = "TEXT")
    private String detalles;
}