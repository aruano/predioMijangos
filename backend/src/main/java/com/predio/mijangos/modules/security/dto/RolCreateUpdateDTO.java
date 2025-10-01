package com.predio.mijangos.modules.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

/** Payload para crear/editar roles. */
public record RolCreateUpdateDTO(
    @NotBlank @Size(max = 50) String nombre,
    @Size(max = 200) String descripcion,
    Boolean admin,
    List<Integer> paginaIds
) {}
