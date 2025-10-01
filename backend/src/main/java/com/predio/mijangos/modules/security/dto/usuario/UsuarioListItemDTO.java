package com.predio.mijangos.modules.security.dto.usuario;

import java.util.Set;

/**
 * DTO de salida para listados paginados de usuarios.
 * Contiene información mínima para tablas y autocompletados.
 *
 * Invariantes:
 * - id no nulo en respuestas del backend.
 * - activo nunca nulo (default true).
 */
public record UsuarioListItemDTO(
    Integer id,
    String usuario,
    String correo,
    Boolean activo,
    Set<String> roles // nombres de rol resumidos
) {}
