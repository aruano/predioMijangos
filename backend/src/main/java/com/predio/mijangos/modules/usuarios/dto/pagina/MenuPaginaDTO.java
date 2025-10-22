// ==================== PÁGINA DTOs ====================

package com.predio.mijangos.modules.usuarios.dto.pagina;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * DTO para páginas en el menú.
 */
@Builder
@Schema(description = "Página en el menú")
public record MenuPaginaDTO(
    
    @Schema(description = "ID de la página", example = "1")
    Integer id,
    
    @Schema(description = "Nombre", example = "Productos")
    String nombre,
    
    @Schema(description = "Icono", example = "package")
    String icono,
    
    @Schema(description = "Ruta", example = "/productos")
    String ruta,
    
    @Schema(description = "Disponible en móvil", example = "true")
    Boolean movil,
    
    @Schema(description = "Orden", example = "1")
    Integer orden
) {}