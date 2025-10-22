// ==================== MÓDULO DTOs ====================

package com.predio.mijangos.modules.usuarios.dto.modulo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * DTO de respuesta para Módulo.
 */
@Builder
@Schema(description = "Información de un módulo del sistema")
public record ModuloResponseDTO(
    
    @Schema(description = "ID único del módulo", example = "1")
    Integer id,
    
    @Schema(description = "Nombre del módulo", example = "Inventario")
    String nombre,
    
    @Schema(description = "Descripción del módulo", example = "Gestión de productos y stock")
    String descripcion,
    
    @Schema(description = "Orden de visualización", example = "2")
    Integer orden,
    
    @Schema(description = "Estado activo/inactivo", example = "true")
    Boolean activo,
    
    @Schema(description = "Cantidad de páginas en el módulo", example = "5")
    Integer cantidadPaginas
) {}