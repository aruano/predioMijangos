// com.predio.mijangos.modules.security.dto.RolResponseDTO
package com.predio.mijangos.modules.security.dto;

import java.util.List;

/** Respuesta para listar/obtener roles. */
public record RolResponseDTO(
    Integer id,
    String nombre,
    String descripcion,
    boolean admin,
    List<PageDTO> paginas
) {}
