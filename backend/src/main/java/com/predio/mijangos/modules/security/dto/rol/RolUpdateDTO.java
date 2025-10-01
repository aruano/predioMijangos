package com.predio.mijangos.modules.security.dto.rol;

import jakarta.validation.constraints.*;
import java.util.Set;

/**
 * DTO de entrada para actualizar roles.
 */
public record RolUpdateDTO(
    @NotNull Integer id,
    @Size(max = 60) String nombre,
    @Size(max = 180) String descripcion,
    Set<@NotNull Integer> paginaIds
) {}
