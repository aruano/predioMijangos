package com.predio.mijangos.modules.security.dto.usuario;

import jakarta.validation.constraints.*;
import java.util.Set;

/**
 * DTO de entrada para actualizar parcialmente un usuario.
 * El password es opcional; si viene se sobrescribe (tras codificación).
 */
public record UsuarioUpdateDTO(
    @NotNull Integer id,
    @Size(max = 60) String usuario,
    @Size(min = 6, max = 120) String password,
    @Email @Size(max = 120) String correo,
    Set<@NotNull Integer> roleIds,
    Boolean activo
) {}
