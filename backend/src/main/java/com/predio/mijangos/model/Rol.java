package com.predio.mijangos.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TBL_Rol")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String nombre;
}
