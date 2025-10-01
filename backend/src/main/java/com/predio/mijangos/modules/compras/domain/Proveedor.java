package com.predio.mijangos.modules.compras.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad JPA que representa un proveedor dentro del módulo de Compras. Se
 * persiste en TBL_Proveedor y agrega metadatos de contacto y estado.
 *
 * Campos críticos: - nombre: usado para búsqueda/ordenamiento; debe estar
 * indexado. - activo: habilita/deshabilita operaciones de compra; indexado para
 * filtros.
 */
@Entity
@Table(name = "TBL_Proveedor",
        indexes = {
            @Index(name = "idx_proveedor_nombre", columnList = "nombre"),
            @Index(name = "idx_proveedor_correo", columnList = "correo")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proveedor {

    /**
     * Identificador técnico autoincremental.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre del proveedor empresarial o individual
     */
    @Column(length = 100, unique = true, nullable = false)
    private String nombre;

    /**
     * Dirección domiciliar del proveedor (opcional)
     */
    @Column(length = 150)
    private String direccion;

    /**
     * Teléfono de contacto.
     */
    @Column(length = 15)
    private String telefono;

    /**
     * Celular de contacto
     */
    @Column(length = 15)
    private String celular;

    /**
     * Correo electrónico de contacto.
     */
    @Column(length = 100)
    private String correo;

    /**
     * Observaciones o peculiaridades del proveedor
     */
    @Column(length = 300)
    private String observaciones;

    /**
     * Flag de status del proveedor (habilita / deshabilita al proveedor)
     */
    private Boolean activo;
}
