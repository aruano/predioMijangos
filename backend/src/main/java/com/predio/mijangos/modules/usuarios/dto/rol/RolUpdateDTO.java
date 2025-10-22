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
 * DTO para actualizar un Rol.
 */
@Builder
@Schema(description = "Datos para actualizar un rol")
public record RolUpdateDTO(
    
    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Schema(description = "Nombre único del rol", example = "SUPERVISOR")
    String nombre,
    
    @Size(max = 100, message = "La descripción no puede exceder 100 caracteres")
    @Schema(description = "Descripción del rol", example = "Supervisor de ventas")
    String descripcion,
    
    @Schema(description = "Es rol de administrador", example = "false")
    Boolean admin,
    
    @Schema(description = "IDs de páginas asignadas al rol", example = "[1, 2, 3]")
    Set<Integer> paginasIds
) {}
