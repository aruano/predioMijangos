package com.predio.mijangos.modules.security.dto.rol;

/**
 * DTO resumido de rol para listados.
 */
public record RolListItemDTO(
    Integer id,
    String nombre,
    String descripcion
) {}
