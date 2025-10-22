package com.predio.mijangos.modules.usuarios.domain;

import com.predio.mijangos.core.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad Usuario - Representa un usuario del sistema con credenciales de acceso.
 * Contiene una relación Many-to-One con Persona para la información personal.
 * 
 * @author Predio Mijangos Dev Team
 * @version 1.0
 * @since 2025-10
 */
@Entity
@Table(
    name = "TBL_Usuario",
    indexes = {
        @Index(name = "idx_usuario_persona", columnList = "id_persona"),
        @Index(name = "idx_usuario_activo", columnList = "activo"),
        @Index(name = "idx_usuario_created", columnList = "created_at"),
        @Index(name = "idx_usuario_deleted", columnList = "deleted_at")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = {"persona", "roles", "refreshTokens"})
public class Usuario extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Código de empleado o username único.
     * Ejemplo: ADMIN, VEND001, BOD001
     */
    @NotBlank(message = "El usuario es obligatorio")
    @Size(max = 100, message = "El usuario no puede exceder 100 caracteres")
    @Column(name = "usuario", nullable = false, unique = true, length = 100)
    private String usuario;

    /**
     * Contraseña encriptada con BCrypt.
     * NUNCA almacenar contraseñas en texto plano.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(max = 100, message = "La contraseña encriptada no puede exceder 100 caracteres")
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    /**
     * Relación Many-to-One con Persona.
     * Un usuario está vinculado a una persona.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "id_persona",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_usuario_persona")
    )
    private Persona persona;

    /**
     * Indica si el usuario está activo en el sistema.
     * Los usuarios inactivos no pueden iniciar sesión.
     */
    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;

    /**
     * Relación Many-to-Many con Rol.
     * Un usuario puede tener múltiples roles.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "TBL_Usuario_Rol",
        joinColumns = @JoinColumn(
            name = "id_usuario",
            foreignKey = @ForeignKey(name = "fk_usuario_rol_usuario")
        ),
        inverseJoinColumns = @JoinColumn(
            name = "id_rol",
            foreignKey = @ForeignKey(name = "fk_usuario_rol_rol")
        )
    )
    @Builder.Default
    private Set<Rol> roles = new HashSet<>();

    /**
     * Relación One-to-Many con RefreshToken.
     * Un usuario puede tener múltiples tokens de refresco (múltiples dispositivos).
     */
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<RefreshToken> refreshTokens = new HashSet<>();

    /**
     * Método helper para agregar un rol al usuario.
     * Mantiene la sincronización bidireccional.
     * 
     * @param rol Rol a agregar
     */
    public void addRol(Rol rol) {
        roles.add(rol);
        rol.getUsuarios().add(this);
    }

    /**
     * Método helper para remover un rol del usuario.
     * Mantiene la sincronización bidireccional.
     * 
     * @param rol Rol a remover
     */
    public void removeRol(Rol rol) {
        roles.remove(rol);
        rol.getUsuarios().remove(this);
    }

    /**
     * Método helper para agregar un refresh token.
     * 
     * @param refreshToken Token a agregar
     */
    public void addRefreshToken(RefreshToken refreshToken) {
        refreshTokens.add(refreshToken);
        refreshToken.setUsuario(this);
    }

    /**
     * Verifica si el usuario está activo y no eliminado.
     * 
     * @return true si está activo y no eliminado
     */
    @Transient
    public boolean isActivoYNoEliminado() {
        return activo && !isDeleted();
    }

    /**
     * Verifica si el usuario tiene un rol específico.
     * 
     * @param nombreRol Nombre del rol a verificar
     * @return true si tiene el rol, false en caso contrario
     */
    @Transient
    public boolean tieneRol(String nombreRol) {
        return roles.stream()
            .anyMatch(rol -> rol.getNombre().equals(nombreRol));
    }

    /**
     * Verifica si el usuario es administrador.
     * 
     * @return true si tiene un rol de administrador
     */
    @Transient
    public boolean isAdmin() {
        return roles.stream()
            .anyMatch(Rol::getAdmin);
    }

    /**
     * Obtiene el nombre completo del usuario desde la persona asociada.
     * 
     * @return Nombre completo
     */
    @Transient
    public String getNombreCompleto() {
        return persona != null ? persona.getNombreCompleto() : "";
    }
}