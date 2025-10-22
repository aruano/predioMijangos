package com.predio.mijangos.security.dto;

import com.predio.mijangos.core.constants.ValidationConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para solicitud de login (autenticación).
 * 
 * <p>Contiene las credenciales del usuario (código de empleado y contraseña)
 * necesarias para autenticarse en el sistema.
 * 
 * <p><b>Ejemplo de uso:</b>
 * <pre>
 * {
 *   "username": "ADMIN",
 *   "password": "Admin123!"
 * }
 * </pre>
 * 
 * @param username Código de empleado del usuario
 * @param password Contraseña del usuario
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
@Schema(description = "Credenciales de login para autenticación")
public record LoginRequestDTO(
        
        @Schema(
                description = "Código de empleado del usuario",
                example = "ADMIN",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = ValidationConstants.USUARIO_REQUIRED_MESSAGE)
        @Size(
                min = ValidationConstants.USUARIO_MIN_LENGTH,
                max = ValidationConstants.USUARIO_MAX_LENGTH,
                message = ValidationConstants.USUARIO_SIZE_MESSAGE
        )
        String username,
        
        @Schema(
                description = "Contraseña del usuario",
                example = "Admin123!",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(
                min = ValidationConstants.PASSWORD_MIN_LENGTH,
                message = "La contraseña debe tener al menos " + 
                          ValidationConstants.PASSWORD_MIN_LENGTH + " caracteres"
        )
        String password
) {
}
