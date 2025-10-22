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
 * DTO para asignar páginas a un rol.
 */
@Builder
@Schema(description = "Datos para asignar páginas a un rol")
public record RolPaginasDTO(
    
    @Schema(description = "IDs de las páginas a asignar", example = "[1, 2, 3, 4]")
    Set<Integer> paginasIds
) {}