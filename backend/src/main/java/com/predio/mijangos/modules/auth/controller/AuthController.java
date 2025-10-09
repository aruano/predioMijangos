package com.predio.mijangos.modules.auth.controller;

import com.predio.mijangos.core.response.ApiResponse;
import com.predio.mijangos.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador de autenticación.
 * 
 * Endpoints públicos para:
 * - Login (POST /api/auth/login)
 * - Refresh token (POST /api/auth/refresh)
 * - Logout (POST /api/auth/logout)
 * 
 * Estos endpoints NO requieren autenticación JWT.
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Autenticación", description = "Endpoints para autenticación y gestión de tokens")
public class AuthController {

    private final AuthenticationService authenticationService;

    /**
     * Endpoint de login.
     * Autentica un usuario y devuelve tokens JWT.
     * 
     * @param request Credenciales de login
     * @return Access token y refresh token
     */
    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario con código de empleado y contraseña. " +
                         "Devuelve access token (8h) y refresh token (7d)."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Login exitoso",
            content = @Content(schema = @Schema(implementation = LoginResponse.class))
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Credenciales inválidas"
    )
    public ResponseEntity<ApiResponse<Map<String, Object>>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        log.info("Solicitud de login para usuario: {}", request.getUsername());
        
        Map<String, Object> tokens = authenticationService.login(
                request.getUsername(),
                request.getPassword()
        );
        
        return ResponseEntity.ok(
                ApiResponse.ok("Login exitoso", tokens)
        );
    }

    /**
     * Endpoint para refrescar access token.
     * Usa un refresh token válido para obtener un nuevo access token.
     * 
     * @param request Refresh token
     * @return Nuevo access token
     */
    @PostMapping("/refresh")
    @Operation(
            summary = "Refrescar access token",
            description = "Obtiene un nuevo access token usando un refresh token válido. " +
                         "No invalida el refresh token (puede reutilizarse)."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Token refrescado exitosamente"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Refresh token inválido o expirado"
    )
    public ResponseEntity<ApiResponse<Map<String, Object>>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        log.debug("Solicitud de refresh token");
        
        Map<String, Object> tokens = authenticationService.refreshAccessToken(
                request.getRefreshToken()
        );
        
        return ResponseEntity.ok(
                ApiResponse.ok("Token refrescado exitosamente", tokens)
        );
    }

    /**
     * Endpoint de logout.
     * Invalida el refresh token del usuario.
     * 
     * @param request Refresh token a invalidar
     * @return Confirmación de logout
     */
    @PostMapping("/logout")
    @Operation(
            summary = "Cerrar sesión",
            description = "Invalida el refresh token y cierra la sesión del usuario. " +
                         "El access token seguirá siendo válido hasta su expiración natural."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Sesión cerrada exitosamente"
    )
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody LogoutRequest request
    ) {
        log.info("Solicitud de logout");
        
        authenticationService.logout(request.getRefreshToken());
        
        return ResponseEntity.ok(
                ApiResponse.ok("Sesión cerrada exitosamente", null)
        );
    }

    /**
     * Endpoint para verificar si un token es válido.
     * Útil para el frontend para verificar autenticación.
     */
    @GetMapping("/verify")
    @Operation(
            summary = "Verificar autenticación",
            description = "Verifica si hay un usuario autenticado en el contexto actual."
    )
    public ResponseEntity<ApiResponse<Map<String, Object>>> verifyAuthentication() {
        try {
            var currentUser = authenticationService.getCurrentUser();
            
            Map<String, Object> userInfo = Map.of(
                    "authenticated", true,
                    "username", currentUser.getUsername(),
                    "authorities", currentUser.getAuthorities()
            );
            
            return ResponseEntity.ok(
                    ApiResponse.ok("Usuario autenticado", userInfo)
            );
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "No autenticado"));
        }
    }

    // ==================== DTOs ====================

    /**
     * DTO para solicitud de login.
     */
    @Schema(description = "Credenciales de login")
    public static class LoginRequest {
        
        @Schema(description = "Código de empleado", example = "EMP001")
        @NotBlank(message = "El código de empleado es requerido")
        @Size(min = 3, max = 50, message = "El código debe tener entre 3 y 50 caracteres")
        private String username;
        
        @Schema(description = "Contraseña", example = "password123")
        @NotBlank(message = "La contraseña es requerida")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        private String password;

        // Getters y Setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    /**
     * DTO para respuesta de login.
     */
    @Schema(description = "Respuesta de login con tokens")
    public static class LoginResponse {
        
        @Schema(description = "Access token JWT", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        private String accessToken;
        
        @Schema(description = "Refresh token", example = "550e8400-e29b-41d4-a716-446655440000")
        private String refreshToken;
        
        @Schema(description = "Tipo de token", example = "Bearer")
        private String tokenType;
        
        @Schema(description = "Tiempo de expiración en segundos", example = "28800")
        private Integer expiresIn;
        
        @Schema(description = "Código de empleado del usuario", example = "EMP001")
        private String username;
        
        @Schema(description = "Roles del usuario", example = "[\"ADMIN\", \"OPERADOR\"]")
        private java.util.List<String> roles;
    }

    /**
     * DTO para solicitud de refresh token.
     */
    @Schema(description = "Solicitud de refresh token")
    public static class RefreshTokenRequest {
        
        @Schema(description = "Refresh token", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotBlank(message = "El refresh token es requerido")
        private String refreshToken;

        // Getters y Setters
        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    }

    /**
     * DTO para solicitud de logout.
     */
    @Schema(description = "Solicitud de logout")
    public static class LogoutRequest {
        
        @Schema(description = "Refresh token a invalidar", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotBlank(message = "El refresh token es requerido")
        private String refreshToken;

        // Getters y Setters
        public String getRefreshToken() { return refreshToken; }
        public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    }
}