package com.predio.mijangos.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para solicitud de renovación de access token.
 * 
 * <p>Contiene el refresh token que se utiliza para obtener
 * un nuevo access token sin necesidad de volver a autenticarse
 * con usuario y contraseña.
 * 
 * <p><b>Flujo de uso:</b>
 * <ol>
 *   <li>Cliente detecta que el access token expiró (401 Unauthorized)</li>
 *   <li>Cliente envía el refresh token almacenado</li>
 *   <li>Si el refresh token es válido, servidor devuelve nuevo access token</li>
 *   <li>Cliente continúa usando la aplicación sin re-autenticarse</li>
 * </ol>
 * 
 * <p><b>Ejemplo de uso:</b>
 * <pre>
 * {
 *   "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
 * }
 * </pre>
 * 
 * @param refreshToken Token de refresco válido obtenido en el login
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
@Schema(description = "Solicitud para renovar el access token usando refresh token")
public record RefreshTokenRequestDTO(
        
        @Schema(
                description = "Refresh token válido obtenido en el login",
                example = "550e8400-e29b-41d4-a716-446655440000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "El refresh token es obligatorio")
        String refreshToken
) {
}
