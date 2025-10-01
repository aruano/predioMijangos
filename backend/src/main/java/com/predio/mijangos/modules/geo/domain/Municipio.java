package com.predio.mijangos.modules.geo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Entidad JPA que representa un municipio dentro del módulo de Geo. Se
 * persiste en TBL_Municipio y agrega datos de nombre y departamento.
 *
 * Campos críticos: - nombre: usado para búsqueda/ordenamiento; debe estar
 * indexado. - departamento asociado a municipio.
 */
@Entity
@Table(name = "TBL_Municipio",
        indexes = {
            @Index(name = "idx_municipio_nombre", columnList = "nombre") 
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Municipio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_departamento")
    private Departamento departamento;
}
