package com.predio.mijangos.modules.security.dto;

import java.util.Set;

/** DTO de lectura para usuario */
public record UsuarioDTO(
        Integer id,
        String usuario,
        boolean activo,
        Set<String> roles
) {}
