package com.predio.mijangos.modules.usuarios.domain;

import com.predio.mijangos.core.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad Rol - Representa un rol de usuario en el sistema.
 * Un rol agrupa permisos y se asigna a usuarios.
 * 
 * @author Predio Mijangos Dev Team
 * @version 1.0
 * @since 2025-10
 */
@Entity
@Table(
    name = "TBL_Rol",
    indexes = {
        @Index(name = "idx_rol_nombre", columnList = "nombre"),
        @Index(name = "idx_rol_activo", columnList = "activo"),
        @Index(name = "idx_rol_created", columnList = "created_at"),
        @Index(name = "idx_rol_deleted", columnList = "deleted_at")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = {"usuarios", "paginas"})
public class Rol extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre único del rol.
     * Ejemplo: ADMIN, SUPERVISOR, VENDEDOR, BODEGUERO
     */
    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    /**
     * Descripción del rol y sus responsabilidades.
     */
    @Size(max = 100, message = "La descripción no puede exceder 100 caracteres")
    @Column(name = "descripcion", length = 100)
    private String descripcion;

    /**
     * Indica si es un rol de administrador con acceso total.
     */
    @Column(name = "admin", nullable = false)
    @Builder.Default
    private Boolean admin = false;

    /**
     * Indica si el rol está activo.
     * Los roles inactivos no pueden ser asignados a nuevos usuarios.
     */
    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;

    /**
     * Relación Many-to-Many con Usuario (lado inverso).
     * Un rol puede ser asignado a múltiples usuarios.
     */
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Usuario> usuarios = new HashSet<>();

    /**
     * Relación Many-to-Many con Pagina.
     * Un rol puede tener acceso a múltiples páginas.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "TBL_Pagina_Rol",
        joinColumns = @JoinColumn(
            name = "id_rol",
            foreignKey = @ForeignKey(name = "fk_pagina_rol_rol")
        ),
        inverseJoinColumns = @JoinColumn(
            name = "id_pagina",
            foreignKey = @ForeignKey(name = "fk_pagina_rol_pagina")
        )
    )
    @Builder.Default
    private Set<Pagina> paginas = new HashSet<>();

    /**
     * Método helper para agregar una página al rol.
     * Mantiene la sincronización bidireccional.
     * 
     * @param pagina Página a agregar
     */
    public void addPagina(Pagina pagina) {
        paginas.add(pagina);
        pagina.getRoles().add(this);
    }

    /**
     * Método helper para remover una página del rol.
     * Mantiene la sincronización bidireccional.
     * 
     * @param pagina Página a remover
     */
    public void removePagina(Pagina pagina) {
        paginas.remove(pagina);
        pagina.getRoles().remove(this);
    }

    /**
     * Verifica si el rol está activo y no eliminado.
     * 
     * @return true si está activo y no eliminado
     */
    @Transient
    public boolean isActivoYNoEliminado() {
        return activo && !isDeleted();
    }

    /**
     * Verifica si el rol tiene acceso a una página específica.
     * 
     * @param nombrePagina Nombre de la página
     * @return true si tiene acceso, false en caso contrario
     */
    @Transient
    public boolean tieneAccesoAPagina(String nombrePagina) {
        return paginas.stream()
            .anyMatch(pagina -> pagina.getNombre().equals(nombrePagina));
    }

    /**
     * Obtiene la cantidad de usuarios asignados a este rol.
     * 
     * @return Cantidad de usuarios
     */
    @Transient
    public int getCantidadUsuarios() {
        return usuarios.size();
    }

    /**
     * Obtiene la cantidad de páginas asignadas a este rol.
     * 
     * @return Cantidad de páginas
     */
    @Transient
    public int getCantidadPaginas() {
        return paginas.size();
    }
}