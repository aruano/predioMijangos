// ==================== DTO SIMPLIFICADO ====================

package com.predio.mijangos.modules.geo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * DTO simplificado para uso en combos y selects.
 */
@Builder
@Schema(description = "Información básica para combos/selects")
public record SimpleItemDTO(
    
    @Schema(description = "ID del item", example = "1")
    Integer id,
    
    @Schema(description = "Nombre del item", example = "Guatemala")
    String nombre
) {}