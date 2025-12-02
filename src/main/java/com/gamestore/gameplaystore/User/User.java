package com.gamestore.gameplaystore.User;

import com.gamestore.gameplaystore.Persona.Persona;
import com.gamestore.gameplaystore.Rol.Rol;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.SuperBuilder;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios") // Coincide con tu data.sql
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends Persona implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING) // Guarda "ADMIN" en la BD, no números
    @Column(nullable = false)
    private Rol rol;

    // --- MÉTODOS DE SPRING SECURITY (UserDetails) ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Spring Security espera "ROLE_ADMIN". Nosotros guardamos "ADMIN".
        // Aquí hacemos la conversión automática.
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public String getUsername() {
        return this.getEmail(); // Usamos el email como usuario para login
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    // Configuración estándar de cuentas (puedes personalizarla si implementas bloqueo de cuentas)
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
