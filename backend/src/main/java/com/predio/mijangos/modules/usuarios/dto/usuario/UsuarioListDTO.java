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

// ==================== LIST DTO ====================

/**
 * DTO simplificado para listados de usuarios.
 */
@Builder
@Schema(description = "Información resumida de un usuario para listados")
public record UsuarioListDTO(
    
    @Schema(description = "ID único del usuario", example = "1")
    Integer id,
    
    @Schema(description = "Código de usuario", example = "VEND001")
    String usuario,
    
    @Schema(description = "Nombre completo", example = "Juan Carlos García")
    String nombreCompleto,
    
    @Schema(description = "Correo electrónico", example = "juan.garcia@example.com")
    String correo,
    
    @Schema(description = "Estado activo/inactivo", example = "true")
    Boolean activo,
    
    @Schema(description = "Cantidad de roles asignados", example = "2")
    Integer cantidadRoles,
    
    @Schema(description = "Nombres de los roles", example = "VENDEDOR, OPERADOR")
    String nombresRoles
) {}
