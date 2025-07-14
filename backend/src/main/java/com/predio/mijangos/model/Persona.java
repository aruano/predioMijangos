package com.predio.mijangos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TBL_Persona")
@Data // Incluye getters, setters, toString, equals, hashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder // Permite usar patrón builder para crear objetos
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String tipo_identificacion;
    
    @Column(unique = true, nullable = false)
    private String identificacion;

    @Column(nullable = false)
    private String nombres;
    
    @Column(nullable = false)
    private String apellidos;
    
    private String correo;
    private String telefono;
    private String direccion;
}
