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
 * DTO simplificado de Rol.
 */
@Builder
@Schema(description = "Información básica de un rol")
public record RolSimpleDTO(
    
    @Schema(description = "ID único del rol", example = "1")
    Integer id,
    
    @Schema(description = "Nombre del rol", example = "SUPERVISOR")
    String nombre,
    
    @Schema(description = "Descripción del rol", example = "Supervisor de ventas")
    String descripcion,
    
    @Schema(description = "Es rol de administrador", example = "false")
    Boolean admin
) {}