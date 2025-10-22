// ==================== DEPARTAMENTO DTOs ====================

package com.predio.mijangos.modules.geo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * DTO de respuesta para Departamento.
 * Solo lectura - los departamentos son catálogos inmutables.
 */
@Builder
@Schema(description = "Información de un departamento de Guatemala")
public record DepartamentoResponseDTO(
    
    @Schema(description = "ID único del departamento", example = "1")
    Integer id,
    
    @Schema(description = "Nombre del departamento", example = "Guatemala")
    String nombre,
    
    @Schema(description = "Cantidad de municipios en el departamento", example = "17")
    Integer cantidadMunicipios
) {}