// ==================== PÁGINA DTOs ====================

package com.predio.mijangos.modules.usuarios.dto.pagina;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * DTO de respuesta para Página.
 */
@Builder
@Schema(description = "Información completa de una página")
public record PaginaResponseDTO(
    
    @Schema(description = "ID único de la página", example = "1")
    Integer id,
    
    @Schema(description = "ID del módulo", example = "1")
    Integer idModulo,
    
    @Schema(description = "Nombre del módulo", example = "Inventario")
    String nombreModulo,
    
    @Schema(description = "Nombre de la página", example = "Productos")
    String nombre,
    
    @Schema(description = "Descripción", example = "Catálogo de productos")
    String descripcion,
    
    @Schema(description = "Disponible en móvil", example = "true")
    Boolean movil,
    
    @Schema(description = "Icono web", example = "package")
    String iconWeb,
    
    @Schema(description = "Icono móvil", example = "package")
    String iconMovil,
    
    @Schema(description = "Ruta web", example = "/productos")
    String redirectWeb,
    
    @Schema(description = "Ruta móvil", example = "/productos")
    String redirectMovil,
    
    @Schema(description = "Orden", example = "1")
    Integer orden,
    
    @Schema(description = "Estado activo", example = "true")
    Boolean activo,
    
    @Schema(description = "Cantidad de roles con acceso", example = "3")
    Integer cantidadRoles
) {}