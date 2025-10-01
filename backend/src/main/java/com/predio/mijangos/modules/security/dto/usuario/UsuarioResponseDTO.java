package com.predio.mijangos.modules.security.dto.usuario;

import java.util.Set;

/**
 * DTO de respuesta con el detalle completo del usuario.
 * No expone la contraseña; maneja roles por nombre y/o id.
 */
public record UsuarioResponseDTO(
    Integer id,
    String usuario,
    String correo,
    Boolean activo,
    Set<Integer> roleIds,
    Set<String> roles
) {}
