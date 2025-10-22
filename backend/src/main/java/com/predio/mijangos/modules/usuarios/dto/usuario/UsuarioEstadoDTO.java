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

// ==================== ACTIVAR/DESACTIVAR DTO ====================

/**
 * DTO para activar o desactivar un usuario.
 */
@Builder
@Schema(description = "Datos para cambiar el estado de un usuario")
public record UsuarioEstadoDTO(
    
    @NotNull(message = "El estado es obligatorio")
    @Schema(description = "Nuevo estado del usuario", example = "true")
    Boolean activo,
    
    @Size(max = 255, message = "El motivo no puede exceder 255 caracteres")
    @Schema(description = "Motivo del cambio de estado", example = "Suspensión temporal")
    String motivo
) {}