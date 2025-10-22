// ==================== PÁGINA DTOs ====================

package com.predio.mijangos.modules.usuarios.dto.pagina;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;


/**
 * DTO para menú agrupado por módulos.
 */
@Builder
@Schema(description = "Estructura del menú por módulo")
public record MenuModuloDTO(
    
    @Schema(description = "ID del módulo", example = "1")
    Integer id,
    
    @Schema(description = "Nombre del módulo", example = "Inventario")
    String nombre,
    
    @Schema(description = "Orden del módulo", example = "2")
    Integer orden,
    
    @Schema(description = "Páginas del módulo")
    java.util.List<MenuPaginaDTO> paginas
) {}