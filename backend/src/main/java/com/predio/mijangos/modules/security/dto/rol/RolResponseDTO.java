package com.predio.mijangos.modules.security.dto.rol;

import java.util.Set;

/**
 * DTO de respuesta de rol con detalle de páginas asociadas.
 */
public record RolResponseDTO(
    Integer id,
    String nombre,
    String descripcion,
    Set<Integer> paginaIds,
    Set<String> paginas // paths o códigos de página
) {}
