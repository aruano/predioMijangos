// ==================== MUNICIPIO DTOs ====================

package com.predio.mijangos.modules.geo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * DTO de respuesta para Municipio.
 * Solo lectura - los municipios son catálogos inmutables.
 */
@Builder
@Schema(description = "Información de un municipio")
public record MunicipioResponseDTO(
    
    @Schema(description = "ID único del municipio", example = "1")
    Integer id,
    
    @Schema(description = "Nombre del municipio", example = "Guatemala")
    String nombre,
    
    @Schema(description = "ID del departamento", example = "1")
    Integer idDepartamento,
    
    @Schema(description = "Nombre del departamento", example = "Guatemala")
    String nombreDepartamento
) {}