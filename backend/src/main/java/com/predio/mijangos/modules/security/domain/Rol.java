package com.predio.mijangos.modules.security.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 *
 * Invariantes: nombre único, no nulo.
 */
/**
 * Entidad JPA que representa un Catálogo de roles del sistema (e.g., VENDEDOR,
 * ADMIN, OFICINA, SUPERVISOR). persiste en TBL_Rol y agrega metadatos.
 *
 * Campos críticos: - nombre: usado para búsqueda/ordenamiento; debe estar
 * indexado. - admin: habilita/deshabilita operaciones de administrador;
 * indexado para filtros.
 */
@Entity
@Table(name = "TBL_Rol", uniqueConstraints = {
    @UniqueConstraint(name = "uk_rol_nombre", columnNames = "nombre")
}, indexes = {
    @Index(name = "idx_rol_nombre", columnList = "nombre"),})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rol {

    /**
     * Identificador técnico autoincremental.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre corto e inmutable del rol.
     */
    @Column(length = 50, nullable = false)
    private String nombre;

    /**
     * Descripción legible del rol.
     */
    @Column(length = 200)
    private String descripcion;

    /**
     * Flag de rol administrativo (facilita políticas UI).
     */
    @Column(nullable = false)
    private boolean admin;
}
