package com.predio.mijangos.modules.security.dto;

import com.predio.mijangos.modules.personas.dto.PersonaResponseDTO;
import java.util.List;

/** Respuesta de usuario para list y get. */
public record UsuarioResponseDTO(
    Integer id,
    String usuario,
    Boolean activo,
    PersonaResponseDTO persona,
    List<RolDTO> roles
) {}
