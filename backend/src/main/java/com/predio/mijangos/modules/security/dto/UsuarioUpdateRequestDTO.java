package com.predio.mijangos.modules.security.dto;

import com.predio.mijangos.modules.personas.dto.PersonaResponseDTO;
import jakarta.validation.constraints.*;
import java.util.List;

/** Request para actualizar usuario.*/
public record UsuarioUpdateRequestDTO(
    @Size(min = 6, max = 120) String password,
    PersonaResponseDTO persona,
    List<Integer> rolesIds,
    Boolean activo
) {}
