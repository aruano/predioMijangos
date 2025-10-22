// ==================== ROL DTOs ====================

package com.predio.mijangos.modules.usuarios.dto.rol;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;


/**
 * DTO para cambiar estado de Rol.
 */
@Builder
@Schema(description = "Datos para cambiar el estado de un rol")
public record RolEstadoDTO(
    
    @NotNull(message = "El estado es obligatorio")
    @Schema(description = "Nuevo estado del rol", example = "true")
    Boolean activo
) {}