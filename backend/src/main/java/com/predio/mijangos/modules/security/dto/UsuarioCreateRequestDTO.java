package com.predio.mijangos.modules.security.dto;

import com.predio.mijangos.modules.personas.dto.PersonaResponseDTO;
import jakarta.validation.constraints.*;
import java.util.List;

/** Request para crear usuario con su Persona y Roles. */
public record UsuarioCreateRequestDTO(
    @NotBlank @Size(max = 50) String usuario,
    @NotBlank @Size(min = 6, max = 120) String password,
    PersonaResponseDTO persona,
    @NotNull List<Integer> rolesIds,
    Boolean activo
) {}
