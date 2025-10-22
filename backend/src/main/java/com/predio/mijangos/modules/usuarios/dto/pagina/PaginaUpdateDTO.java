// ==================== PÁGINA DTOs ====================

package com.predio.mijangos.modules.usuarios.dto.pagina;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * DTO para actualizar una Página.
 */
@Builder
@Schema(description = "Datos para actualizar una página")
public record PaginaUpdateDTO(
    
    @NotNull(message = "El ID del módulo es obligatorio")
    @Schema(description = "ID del módulo al que pertenece", example = "1")
    Integer idModulo,
    
    @NotBlank(message = "El nombre de la página es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Schema(description = "Nombre único de la página", example = "Productos")
    String nombre,
    
    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    @Schema(description = "Descripción de la página", example = "Catálogo de productos")
    String descripcion,
    
    @Schema(description = "Disponible en móvil", example = "true")
    Boolean movil,
    
    @Size(max = 100, message = "El icono no puede exceder 100 caracteres")
    @Schema(description = "Icono para web", example = "package")
    String iconWeb,
    
    @Size(max = 100, message = "El icono no puede exceder 100 caracteres")
    @Schema(description = "Icono para móvil", example = "package")
    String iconMovil,
    
    @Size(max = 100, message = "La ruta no puede exceder 100 caracteres")
    @Schema(description = "Ruta de redirección web", example = "/productos")
    String redirectWeb,
    
    @Size(max = 100, message = "La ruta no puede exceder 100 caracteres")
    @Schema(description = "Ruta de redirección móvil", example = "/productos")
    String redirectMovil,
    
    @Schema(description = "Orden de visualización", example = "1")
    Integer orden,
    
    @Schema(description = "Estado activo/inactivo", example = "true")
    Boolean activo
) {}