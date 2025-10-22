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
 * DTO de respuesta completa para Rol.
 */
@Builder
@Schema(description = "Información completa de un rol")
public record RolResponseDTO(
    
    @Schema(description = "ID único del rol", example = "1")
    Integer id,
    
    @Schema(description = "Nombre del rol", example = "SUPERVISOR")
    String nombre,
    
    @Schema(description = "Descripción del rol", example = "Supervisor de ventas")
    String descripcion,
    
    @Schema(description = "Es rol de administrador", example = "false")
    Boolean admin,
    
    @Schema(description = "Estado activo/inactivo", example = "true")
    Boolean activo,
    
    @Schema(description = "Cantidad de usuarios con este rol", example = "5")
    Integer cantidadUsuarios,
    
    @Schema(description = "Cantidad de páginas asignadas", example = "10")
    Integer cantidadPaginas,
    
    @Schema(description = "Páginas asignadas al rol")
    Set<PaginaSimpleDTO> paginas,
    
    @Schema(description = "Fecha de creación", example = "2025-10-10T10:30:00")
    LocalDateTime createdAt,
    
    @Schema(description = "Fecha de última actualización", example = "2025-10-10T15:45:00")
    LocalDateTime updatedAt
) {}