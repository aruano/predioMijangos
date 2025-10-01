// modules/geo/dto/MunicipioDTO.java
package com.predio.mijangos.modules.geo.dto;

/**
 * DTO de salida para listados paginados de municipios. Contiene el subconjunto
 * mínimo para tablas y autocompletados.
 *
 * Invariantes: - id no nulo en respuestas del backend. - departamento nunca nulo
 */
public record MunicipioResponseDTO(
        Integer id,
        String nombre, 
        Integer departamentoId) {

}
