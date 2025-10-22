package com.predio.mijangos.modules.geo.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad Municipio - Catálogo de municipios por departamento.
 * Esta es una tabla de catálogo inmutable, no extiende BaseEntity.
 * 
 * @author Predio Mijangos Dev Team
 * @version 1.0
 * @since 2025-10
 */
@Entity
@Table(
    name = "TBL_Municipio",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_municipio_nombre_depto",
            columnNames = {"nombre", "id_departamento"}
        )
    },
    indexes = {
        @Index(name = "idx_municipio_departamento", columnList = "id_departamento"),
        @Index(name = "idx_municipio_nombre", columnList = "nombre")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "departamento")
public class Municipio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    /**
     * Relación Many-to-One con Departamento.
     * Un municipio pertenece a un departamento.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "id_departamento",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_municipio_departamento")
    )
    private Departamento departamento;

    /**
     * Constructor de conveniencia.
     * 
     * @param nombre Nombre del municipio
     * @param departamento Departamento al que pertenece
     */
    public Municipio(String nombre, Departamento departamento) {
        this.nombre = nombre;
        this.departamento = departamento;
    }
}