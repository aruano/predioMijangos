package com.predio.mijangos.modules.security.dto.pagina;

/**
 * DTO resumido de página para listados.
 */
public record PaginaListItemDTO(
    Integer id,
    String codigo,  // nombre interno o slug
    String path,    // ruta de frontend /app/...
    String modulo,  // módulo funcional
    Boolean movil   // visible en app móvil
) {}
