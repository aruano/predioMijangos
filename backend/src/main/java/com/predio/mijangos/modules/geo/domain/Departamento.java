package com.predio.mijangos.modules.geo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Departamento - Catálogo de departamentos de Guatemala.
 * Esta es una tabla de catálogo inmutable, no extiende BaseEntity.
 * 
 * @author Predio Mijangos Dev Team
 * @version 1.0
 * @since 2025-10
 */
@Entity
@Table(name = "TBL_Departamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "municipios")
public class Departamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    /**
     * Relación One-to-Many con Municipio.
     * Un departamento puede tener muchos municipios.
     */
    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Municipio> municipios = new ArrayList<>();

    /**
     * Constructor de conveniencia.
     * 
     * @param nombre Nombre del departamento
     */
    public Departamento(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método helper para agregar un municipio.
     * Mantiene la sincronización bidireccional.
     * 
     * @param municipio Municipio a agregar
     */
    public void addMunicipio(Municipio municipio) {
        municipios.add(municipio);
        municipio.setDepartamento(this);
    }

    /**
     * Método helper para remover un municipio.
     * Mantiene la sincronización bidireccional.
     * 
     * @param municipio Municipio a remover
     */
    public void removeMunicipio(Municipio municipio) {
        municipios.remove(municipio);
        municipio.setDepartamento(null);
    }
}