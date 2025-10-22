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

// ==================== CAMBIO DE CONTRASEÑA DTO ====================

/**
 * DTO para cambiar la contraseña de un usuario.
 */
@Builder
@Schema(description = "Datos para cambiar la contraseña")
public record CambioPasswordDTO(
    
    @NotBlank(message = "La contraseña actual es obligatoria")
    @Schema(description = "Contraseña actual", example = "OldPassword123!")
    String passwordActual,
    
    @NotBlank(message = "La nueva contraseña es obligatoria")
    @Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres")
    @Schema(description = "Nueva contraseña", example = "NewPassword123!")
    String passwordNueva,
    
    @NotBlank(message = "La confirmación de contraseña es obligatoria")
    @Schema(description = "Confirmación de la nueva contraseña", example = "NewPassword123!")
    String passwordConfirmacion
) {}