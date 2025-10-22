package com.predio.mijangos.modules.usuarios.dto.usuario;

import com.predio.mijangos.modules.usuarios.dto.persona.PersonaCreateDTO;
import com.predio.mijangos.modules.usuarios.dto.rol.RolSimpleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

// ==================== RESPONSE DTO ====================

/**
 * DTO de respuesta completa para Usuario.
 */
@Builder
@Schema(description = "Información completa de un usuario")
public record UsuarioResponseDTO(
    
    @Schema(description = "ID único del usuario", example = "1")
    Integer id,
    
    @Schema(description = "Código de usuario", example = "VEND001")
    String usuario,
    
    @Schema(description = "ID de la persona asociada", example = "1")
    Integer idPersona,
    
    @Schema(description = "Nombre completo", example = "Juan Carlos García")
    String nombreCompleto,
    
    @Schema(description = "Identificación", example = "1234567890101")
    String identificacion,
    
    @Schema(description = "Correo electrónico", example = "juan.garcia@example.com")
    String correo,
    
    @Schema(description = "Teléfono", example = "50212345678")
    String telefono,
    
    @Schema(description = "Estado activo/inactivo", example = "true")
    Boolean activo,
    
    @Schema(description = "Roles asignados al usuario")
    Set<RolSimpleDTO> roles,
    
    @Schema(description = "Fecha de creación", example = "2025-10-10T10:30:00")
    LocalDateTime createdAt,
    
    @Schema(description = "Fecha de última actualización", example = "2025-10-10T15:45:00")
    LocalDateTime updatedAt
) {}
