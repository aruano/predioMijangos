package com.predio.mijangos.modules.geo.dto;

/**
 * DTO de salida para listados de Departamentos. Contiene el subconjunto mínimo
 * para tablas y autocompletados.
 *
 * Invariantes: - id no nulo en respuestas del backend.
 */
public record DepartamentoResponseDTO(
        Integer id,
        String nombre
        ) {
}
