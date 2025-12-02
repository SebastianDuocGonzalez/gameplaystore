package com.gamestore.gameplaystore.Persona;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder // Permite el builder en herencia
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass // IMPORTANTE: Sus campos van a la tabla del hijo (User)
public abstract class Persona {

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    @Column(nullable = false, unique = true)
    private String email;
    
    // Puedes agregar teléfono, dirección, etc. aquí en el futuro
}
