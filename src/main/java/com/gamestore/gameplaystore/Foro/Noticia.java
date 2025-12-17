package com.gamestore.gameplaystore.Foro;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "noticias")
@Data
public class Noticia {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    @Column(columnDefinition = "TEXT")
    private String contenido;
    private String fuenteUrl;
    private String imagenUrl;
    private LocalDateTime fechaPublicacion;

    @PrePersist
    public void prePersist() { fechaPublicacion = LocalDateTime.now(); }
}