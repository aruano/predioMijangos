package com.predio.mijangos.modules.security.domain;

import com.predio.mijangos.modules.personas.domain.Persona;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.Instant;
import java.util.*;

/**
 * Usuario del sistema con credenciales y relación a roles.
 * Invariantes: usuario único; password encriptado (BCrypt); activo controla el
 * acceso.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TBL_Usuario", uniqueConstraints = {
    @UniqueConstraint(name = "uk_usuario_username", columnNames = "usuario")
})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre de inicio de sesión (único, inmutable una vez creado).
     */
    @Column(name = "usuario", length = 50, nullable = false)
    private String usuario;

    /**
     * Hash de contraseña (BCrypt).
     */
    @Column(name = "password", length = 120, nullable = false)
    private String password;

    /**
     * Activo/inactivo.
     */
    @Column(nullable = false)
    private boolean activo;

    /**
     * Relación opcional con datos personales.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona")
    private Persona persona;

    /**
     * Timestamp de creación (auditoría básica).
     */
    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private Instant fechaCreacion;

    /**
     * Relación con roles mediante tabla intermedia.
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<UsuarioRol> roles = new HashSet<>();

    /**
     * Conveniencia para mantener integridad de la relación.
     */
    public void addRol(Rol rol) {
        UsuarioRol ur = new UsuarioRol();
        ur.setUsuario(this);
        ur.setRol(rol);
        this.roles.add(ur);
    }
}
