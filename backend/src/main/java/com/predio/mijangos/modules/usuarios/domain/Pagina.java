package com.predio.mijangos.modules.usuarios.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Entidad Página - Representa una página o pantalla del sistema.
 * Las páginas pertenecen a módulos y se asignan a roles para control de acceso.
 * No extiende BaseEntity porque es un catálogo relativamente estático.
 * 
 * @author Predio Mijangos Dev Team
 * @version 1.0
 * @since 2025-10
 */
@Entity
@Table(
    name = "TBL_Pagina",
    indexes = {
        @Index(name = "idx_pagina_modulo", columnList = "id_modulo"),
        @Index(name = "idx_pagina_nombre", columnList = "nombre"),
        @Index(name = "idx_pagina_orden", columnList = "orden")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"modulo", "roles"})
public class Pagina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Relación Many-to-One con Módulo.
     * Una página pertenece a un módulo.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "id_modulo",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_pagina_modulo")
    )
    private Modulo modulo;

    /**
     * Nombre único de la página.
     * Ejemplo: Usuarios, Roles, Productos, Nueva Venta
     */
    @NotBlank(message = "El nombre de la página es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    /**
     * Descripción de la funcionalidad de la página.
     */
    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    @Column(name = "descripcion", length = 255)
    private String descripcion;

    /**
     * Indica si la página está disponible en la aplicación móvil.
     */
    @Column(name = "movil", nullable = false)
    @Builder.Default
    private Boolean movil = false;

    /**
     * Icono para la interfaz web.
     * Puede ser el nombre de un icono de una librería (ej: Lucide, FontAwesome)
     */
    @Size(max = 100, message = "El icono web no puede exceder 100 caracteres")
    @Column(name = "icon_web", length = 100)
    private String iconWeb;

    /**
     * Icono para la aplicación móvil.
     */
    @Size(max = 100, message = "El icono móvil no puede exceder 100 caracteres")
    @Column(name = "icon_movil", length = 100)
    private String iconMovil;

    /**
     * Ruta de redirección en la interfaz web.
     * Ejemplo: /usuarios, /productos, /ventas/nueva
     */
    @Size(max = 100, message = "La ruta web no puede exceder 100 caracteres")
    @Column(name = "redirect_web", length = 100)
    private String redirectWeb;

    /**
     * Ruta de redirección en la aplicación móvil.
     */
    @Size(max = 100, message = "La ruta móvil no puede exceder 100 caracteres")
    @Column(name = "redirect_movil", length = 100)
    private String redirectMovil;

    /**
     * Orden de visualización en el menú.
     * Menor número = mayor prioridad.
     */
    @Column(name = "orden")
    @Builder.Default
    private Integer orden = 0;

    /**
     * Indica si la página está activa.
     * Las páginas inactivas no se muestran en el sistema.
     */
    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;

    /**
     * Relación Many-to-Many con Rol (lado inverso).
     * Una página puede ser accedida por múltiples roles.
     */
    @ManyToMany(mappedBy = "paginas", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Rol> roles = new HashSet<>();

    /**
     * Constructor de conveniencia.
     * 
     * @param modulo Módulo al que pertenece
     * @param nombre Nombre de la página
     * @param redirectWeb Ruta web
     */
    public Pagina(Modulo modulo, String nombre, String redirectWeb) {
        this.modulo = modulo;
        this.nombre = nombre;
        this.redirectWeb = redirectWeb;
    }

    /**
     * Verifica si la página está disponible en web.
     * 
     * @return true si tiene ruta web configurada
     */
    @Transient
    public boolean esDisponibleEnWeb() {
        return redirectWeb != null && !redirectWeb.isBlank();
    }

    /**
     * Verifica si la página está disponible en móvil.
     * 
     * @return true si está marcada como móvil y tiene ruta configurada
     */
    @Transient
    public boolean esDisponibleEnMovil() {
        return movil && redirectMovil != null && !redirectMovil.isBlank();
    }

    /**
     * Obtiene la cantidad de roles que tienen acceso a esta página.
     * 
     * @return Cantidad de roles
     */
    @Transient
    public int getCantidadRoles() {
        return roles.size();
    }

    /**
     * Verifica si un rol específico tiene acceso a esta página.
     * 
     * @param nombreRol Nombre del rol
     * @return true si el rol tiene acceso
     */
    @Transient
    public boolean tieneAccesoRol(String nombreRol) {
        return roles.stream()
            .anyMatch(rol -> rol.getNombre().equals(nombreRol));
    }
}