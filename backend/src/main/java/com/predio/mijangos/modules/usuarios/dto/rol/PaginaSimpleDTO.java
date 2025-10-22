// ==================== PÁGINA SIMPLE DTO (usado en Rol) ====================

package com.predio.mijangos.modules.usuarios.dto.rol;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Información básica de una página")
public record PaginaSimpleDTO(
    
    @Schema(description = "ID de la página", example = "1")
    Integer id,
    
    @Schema(description = "Nombre de la página", example = "Usuarios")
    String nombre,
    
    @Schema(description = "Ruta web", example = "/usuarios")
    String redirectWeb,
    
    @Schema(description = "Icono", example = "users")
    String iconWeb
) {}