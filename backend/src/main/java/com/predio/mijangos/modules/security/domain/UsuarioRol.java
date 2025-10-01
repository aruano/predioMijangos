package com.predio.mijangos.modules.security.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Tabla intermedia Usuario↔Rol.
 * Invariantes: combinación (usuario, rol) única.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TBL_Usuario_Rol",
        uniqueConstraints = @UniqueConstraint(name = "uk_usuario_rol", columnNames = {"id_usuario", "id_rol"}))
public class UsuarioRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;
}
