package com.predio.mijangos.modules.geo.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Entidad JPA que representa un departamento dentro del módulo de Geo. Se
 * persiste en TBL_Departamento.
 *
 * Campos críticos: - nombre.
 */
@Entity
@Table(name = "TBL_Departamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(name = "nombre", length = 100, nullable = false, unique = true)
    private String nombre;
}
