// ==================== MÓDULO DTOs ====================

package com.predio.mijangos.modules.usuarios.dto.modulo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * DTO simplificado de Módulo para combos.
 */
@Builder
@Schema(description = "Información básica de un módulo")
public record ModuloSimpleDTO(
    
    @Schema(description = "ID del módulo", example = "1")
    Integer id,
    
    @Schema(description = "Nombre del módulo", example = "Inventario")
    String nombre
) {}