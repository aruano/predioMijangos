package com.predio.mijangos.modules.personas.domain;

import com.predio.mijangos.modules.geo.domain.Municipio;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad JPA que representa una persona dentro de los módulos que 
 * lo necesiten. Se persiste en TBL_Persona y agrega metadatos de contacto y dirección.
 *
 * Campos críticos: - identificación: usado para búsqueda/ordenamiento; debe estar
 * indexado.
 */
@Entity
@Table(name = "TBL_Persona",
        indexes = {
            @Index(name = "idx_persona_identificacion", columnList = "identificacion"),
            @Index(name = "idx_nombres_identificacion", columnList = "nombres"),
            @Index(name = "idx_apellidos_identificacion", columnList = "apellidos")            
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipo_identificacion", length = 20)
    private String tipoIdentificacion;

    @Column(length = 50, unique = true)
    private String identificacion;

    @Column(length = 100)
    private String nombres;
    @Column(length = 100)
    private String apellidos;
    @Column(length = 120)
    private String correo;
    @Column(length = 30)
    private String telefono;

    @Column(name = "direccion_domicilio", length = 200)
    private String direccionDomicilio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_municipio")
    private Municipio municipio;
}
