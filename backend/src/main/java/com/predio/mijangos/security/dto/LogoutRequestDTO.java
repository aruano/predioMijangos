package com.predio.mijangos.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para solicitud de logout (cerrar sesión).
 * 
 * <p>Contiene el refresh token que debe ser invalidado al cerrar sesión.
 * Al invalidar el refresh token, el usuario no podrá obtener nuevos
 * access tokens sin volver a autenticarse.
 * 
 * <p><b>Nota importante:</b> El access token JWT seguirá siendo válido
 * hasta su expiración natural. Por seguridad, el cliente debe eliminar
 * tanto el access token como el refresh token de su almacenamiento local.
 * 
 * <p><b>Ejemplo de uso:</b>
 * <pre>
 * {
 *   "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
 * }
 * </pre>
 * 
 * @param refreshToken Refresh token a invalidar
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
@Schema(description = "Solicitud para cerrar sesión e invalidar el refresh token")
public record LogoutRequestDTO(
        
        @Schema(
                description = "Refresh token a invalidar",
                example = "550e8400-e29b-41d4-a716-446655440000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El refresh token es obligatorio para cerrar sesión")
        String refreshToken
) {
}
