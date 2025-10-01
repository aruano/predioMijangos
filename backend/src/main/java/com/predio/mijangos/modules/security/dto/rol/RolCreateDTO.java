package com.predio.mijangos.modules.security.dto.rol;

import jakarta.validation.constraints.*;
import java.util.Set;

/**
 * DTO de entrada para crear roles.
 */
public record RolCreateDTO(
    @NotBlank @Size(max = 60) String nombre,
    @Size(max = 180) String descripcion,
    @NotNull Set<@NotNull Integer> paginaIds
) {}
