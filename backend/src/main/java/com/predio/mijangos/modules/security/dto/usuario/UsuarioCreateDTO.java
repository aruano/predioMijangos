package com.predio.mijangos.modules.security.dto.usuario;

import jakarta.validation.constraints.*;
import java.util.Set;

/**
 * DTO de entrada para crear un usuario.
 * Validaciones aplicadas en controller con Jakarta Validation.
 *
 * Reglas:
 * - usuario y password obligatorios.
 * - correo (si viene) debe tener formato.
 * - roleIds se utiliza para asignación inicial de roles.
 */
public record UsuarioCreateDTO(
    @NotBlank @Size(max = 60) String usuario,
    @NotBlank @Size(min = 6, max = 120) String password,
    @Email @Size(max = 120) String correo,
    @NotNull Set<@NotNull Integer> roleIds,
    Boolean activo
) {}
