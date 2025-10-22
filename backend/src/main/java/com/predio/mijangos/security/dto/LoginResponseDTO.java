package com.predio.mijangos.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.Set;

/**
 * DTO para respuesta de login exitoso.
 * 
 * <p>Contiene los tokens de autenticación (access token y refresh token)
 * junto con información básica del usuario autenticado.
 * 
 * <p><b>Estructura de respuesta:</b>
 * <pre>
 * {
 *   "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
 *   "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
 *   "tokenType": "Bearer",
 *   "expiresIn": 28800,
 *   "usuario": {
 *     "id": 1,
 *     "usuario": "ADMIN",
 *     "nombreCompleto": "Administrador del Sistema",
 *     "correo": "admin@prediomijangos.com",
 *     "roles": ["ADMIN"]
 *   }
 * }
 * </pre>
 * 
 * @param accessToken Token JWT para autenticar las solicitudes
 * @param refreshToken Token para renovar el access token cuando expire
 * @param tokenType Tipo de token (siempre "Bearer")
 * @param expiresIn Tiempo de expiración del access token en segundos
 * @param usuario Información básica del usuario autenticado
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
@Builder
@Schema(description = "Respuesta de login con tokens y datos del usuario")
public record LoginResponseDTO(
        
        @Schema(
                description = "Access token JWT para autenticar las solicitudes API",
                example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String accessToken,
        
        @Schema(
                description = "Refresh token para renovar el access token cuando expire",
                example = "550e8400-e29b-41d4-a716-446655440000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String refreshToken,
        
        @Schema(
                description = "Tipo de token (Bearer)",
                example = "Bearer",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String tokenType,
        
        @Schema(
                description = "Tiempo de expiración del access token en segundos",
                example = "28800",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        Integer expiresIn,
        
        @Schema(
                description = "Información básica del usuario autenticado",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        UsuarioInfoDTO usuario
) {
    
    /**
     * DTO interno con información básica del usuario autenticado.
     * 
     * <p>Contiene los datos esenciales del usuario que el cliente necesita
     * para personalizar la interfaz y gestionar la sesión.
     * 
     * @param id ID único del usuario
     * @param usuario Código de empleado
     * @param nombreCompleto Nombre completo del usuario
     * @param correo Correo electrónico del usuario
     * @param roles Conjunto de roles asignados al usuario
     */
    @Builder
    @Schema(description = "Información básica del usuario autenticado")
    public record UsuarioInfoDTO(
            
            @Schema(description = "ID único del usuario", example = "1")
            Integer id,
            
            @Schema(description = "Código de empleado", example = "ADMIN")
            String usuario,
            
            @Schema(description = "Nombre completo del usuario", example = "Administrador del Sistema")
            String nombreCompleto,
            
            @Schema(description = "Correo electrónico", example = "admin@prediomijangos.com")
            String correo,
            
            @Schema(description = "Roles del usuario", example = "[\"ADMIN\", \"SUPERVISOR\"]")
            Set<String> roles
    ) {
    }
}
