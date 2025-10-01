package com.predio.mijangos.modules.security.dto.pagina;

/**
 * DTO de detalle de página.
 */
public record PaginaResponseDTO(
    Integer id,
    String codigo,
    String path,
    String modulo,
    String icono,
    Boolean movil,
    Boolean activo
) {}
