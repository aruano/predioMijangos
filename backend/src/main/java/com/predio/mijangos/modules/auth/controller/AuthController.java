package com.predio.mijangos.modules.auth.controller;

import com.predio.mijangos.core.response.ApiResponse;
import com.predio.mijangos.security.dto.LoginRequestDTO;
import com.predio.mijangos.security.dto.LoginResponseDTO;
import com.predio.mijangos.security.dto.LogoutRequestDTO;
import com.predio.mijangos.security.dto.RefreshTokenRequestDTO;
import com.predio.mijangos.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador REST para autenticación y gestión de sesiones.
 * 
 * <p>Proporciona endpoints públicos para el manejo de autenticación
 * de usuarios y gestión de tokens JWT.
 * 
 * <p><b>Endpoints disponibles:</b>
 * <ul>
 *   <li><b>POST /api/auth/login</b> - Iniciar sesión con credenciales</li>
 *   <li><b>POST /api/auth/refresh</b> - Renovar access token</li>
 *   <li><b>POST /api/auth/logout</b> - Cerrar sesión</li>
 *   <li><b>GET /api/auth/verify</b> - Verificar autenticación actual</li>
 * </ul>
 * 
 * <p><b>Seguridad:</b><br>
 * Todos los endpoints excepto /verify son públicos y no requieren
 * autenticación JWT. El endpoint /verify requiere un token válido
 * en el header Authorization.
 * 
 * <p><b>Tokens:</b>
 * <ul>
 *   <li><b>Access Token:</b> JWT válido por 8 horas</li>
 *   <li><b>Refresh Token:</b> UUID válido por 7 días</li>
 * </ul>
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 * 
 * @see AuthenticationService
 * @see LoginRequestDTO
 * @see LoginResponseDTO
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Autenticación", description = "Endpoints para autenticación y gestión de tokens")
public class AuthController {

    private final AuthenticationService authenticationService;

    /**
     * Autentica un usuario y genera tokens de acceso.
     * 
     * <p>Valida las credenciales del usuario (código de empleado y contraseña)
     * y, si son correctas, genera un access token JWT y un refresh token.
     * 
     * <p><b>Proceso:</b>
     * <ol>
     *   <li>Valida las credenciales contra la base de datos</li>
     *   <li>Verifica que el usuario esté activo</li>
     *   <li>Genera access token JWT (válido 8 horas)</li>
     *   <li>Genera refresh token (válido 7 días)</li>
     *   <li>Registra información del dispositivo y ubicación</li>
     *   <li>Retorna ambos tokens con información del usuario</li>
     * </ol>
     * 
     * <p><b>Uso del access token:</b><br>
     * Incluir en el header Authorization de todas las peticiones:
     * <pre>
     * Authorization: Bearer {accessToken}
     * </pre>
     * 
     * <p><b>Uso del refresh token:</b><br>
     * Enviar al endpoint /refresh cuando el access token expire.
     * 
     * @param request DTO con credenciales del usuario (usuario y contraseña)
     * @return ResponseEntity con ApiResponse que contiene LoginResponseDTO
     *         con access token, refresh token e información del usuario
     * 
     * @throws org.springframework.security.authentication.BadCredentialsException
     *         si las credenciales son inválidas (retorna 401)
     * @throws org.springframework.security.authentication.DisabledException
     *         si el usuario está inactivo (retorna 401)
     */
    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica un usuario con código de empleado y contraseña. " +
                         "Devuelve access token (8h) y refresh token (7d) junto con información del usuario."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse (
            responseCode = "200",
            description = "Login exitoso - Tokens generados correctamente",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LoginResponseDTO.class)
            )
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse (
            responseCode = "400",
            description = "Solicitud inválida - Datos faltantes o formato incorrecto"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse (
            responseCode = "401",
            description = "Credenciales inválidas o usuario inactivo"
    )
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO request
    ) {
        log.info("Solicitud de login para usuario: {}", request.username());
        
        // Autenticar y generar tokens
        LoginResponseDTO response = authenticationService.login(request);
        
        log.info("Login exitoso para usuario: {}", request.username());
        
        return ResponseEntity.ok(
                ApiResponse.ok("Login exitoso", response)
        );
    }

    /**
     * Renueva el access token usando un refresh token válido.
     * 
     * <p>Permite al cliente obtener un nuevo access token sin necesidad
     * de volver a ingresar credenciales, mejorando la experiencia del usuario.
     * 
     * <p><b>Proceso:</b>
     * <ol>
     *   <li>Valida que el refresh token exista en la base de datos</li>
     *   <li>Verifica que no esté expirado (7 días)</li>
     *   <li>Verifica que no esté revocado</li>
     *   <li>Verifica que el usuario siga activo</li>
     *   <li>Genera nuevo access token JWT (válido 8 horas)</li>
     *   <li>Retorna nuevo access token (mismo refresh token)</li>
     * </ol>
     * 
     * <p><b>Nota importante:</b><br>
     * El refresh token NO se renueva, solo el access token.
     * Cuando el refresh token expire (7 días), el usuario deberá
     * volver a hacer login con sus credenciales.
     * 
     * <p><b>Uso recomendado:</b><br>
     * El cliente debe llamar a este endpoint automáticamente cuando
     * detecte que el access token está próximo a expirar o ha expirado.
     * 
     * @param request DTO con el refresh token a validar
     * @return ResponseEntity con ApiResponse que contiene LoginResponseDTO
     *         con nuevo access token, mismo refresh token e información actualizada del usuario
     * 
     * @throws org.springframework.security.authentication.BadCredentialsException
     *         si el refresh token es inválido, expirado o revocado (retorna 401)
     * @throws org.springframework.security.authentication.DisabledException
     *         si el usuario está inactivo (retorna 401)
     */
    @PostMapping("/refresh")
    @Operation(
            summary = "Renovar access token",
            description = "Obtiene un nuevo access token usando un refresh token válido. " +
                         "El refresh token no se renueva y puede reutilizarse hasta su expiración."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse (
            responseCode = "200",
            description = "Token renovado exitosamente"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse (
            responseCode = "400",
            description = "Solicitud inválida - Refresh token faltante"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse (
            responseCode = "401",
            description = "Refresh token inválido, expirado o revocado"
    )
    public ResponseEntity<ApiResponse<LoginResponseDTO>> refreshToken(
            @Valid @RequestBody RefreshTokenRequestDTO request
    ) {
        log.info("Solicitud de renovación de access token");
        
        // Renovar access token
        LoginResponseDTO response = authenticationService.refreshAccessToken(
                request.refreshToken()
        );
        
        log.info("Access token renovado exitosamente");
        
        return ResponseEntity.ok(
                ApiResponse.ok("Token renovado exitosamente", response)
        );
    }

    /**
     * Cierra la sesión del usuario invalidando su refresh token.
     * 
     * <p>Revoca el refresh token para que no pueda ser usado nuevamente
     * para renovar access tokens. Esto efectivamente cierra la sesión
     * del usuario en el dispositivo desde el cual se hizo logout.
     * 
     * <p><b>Proceso:</b>
     * <ol>
     *   <li>Busca el refresh token en la base de datos</li>
     *   <li>Lo marca como revocado (revoked = true)</li>
     *   <li>El token ya no puede usarse para renovar access tokens</li>
     * </ol>
     * 
     * <p><b>Nota importante sobre el access token:</b><br>
     * El access token seguirá siendo técnicamente válido hasta su
     * expiración natural (8 horas). Sin embargo, el cliente debe
     * eliminarlo de su almacenamiento inmediatamente para cerrar
     * la sesión de forma efectiva.
     * 
     * <p><b>Mejores prácticas del cliente:</b>
     * <ul>
     *   <li>Eliminar access token del almacenamiento local</li>
     *   <li>Eliminar refresh token del almacenamiento local</li>
     *   <li>Limpiar cualquier caché de datos del usuario</li>
     *   <li>Redirigir al usuario a la página de login</li>
     * </ul>
     * 
     * @param request DTO con el refresh token a invalidar
     * @return ResponseEntity con ApiResponse vacío indicando éxito
     * 
     * @throws com.predio.mijangos.core.exception.ResourceNotFoundException
     *         si el refresh token no existe (retorna 404)
     */
    @PostMapping("/logout")
    @Operation(
            summary = "Cerrar sesión",
            description = "Invalida el refresh token y cierra la sesión del usuario. " +
                         "El access token seguirá siendo válido hasta su expiración natural, " +
                         "por lo que el cliente debe eliminarlo inmediatamente."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse (
            responseCode = "200",
            description = "Sesión cerrada exitosamente"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse (
            responseCode = "400",
            description = "Solicitud inválida - Refresh token faltante"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse (
            responseCode = "404",
            description = "Refresh token no encontrado"
    )
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody LogoutRequestDTO request
    ) {
        log.info("Solicitud de logout");
        
        // Invalidar refresh token
        authenticationService.logout(request.refreshToken());
        
        log.info("Sesión cerrada exitosamente");
        
        return ResponseEntity.ok(
                ApiResponse.ok("Sesión cerrada exitosamente", null)
        );
    }

    /**
     * Verifica si hay un usuario autenticado en el contexto actual.
     * 
     * <p>Útil para que el frontend verifique la validez de la sesión
     * actual sin necesidad de hacer otra llamada al backend.
     * 
     * <p><b>Requisito:</b><br>
     * Este endpoint requiere un access token válido en el header Authorization.
     * 
     * <p><b>Uso típico:</b>
     * <ul>
     *   <li>Verificar autenticación al cargar la aplicación</li>
     *   <li>Validar sesión antes de operaciones críticas</li>
     *   <li>Obtener información actualizada del usuario</li>
     * </ul>
     * 
     * <p><b>Respuesta exitosa:</b>
     * <pre>
     * {
     *   "authenticated": true,
     *   "username": "ADMIN",
     *   "authorities": ["ROLE_ADMIN", "ROLE_SUPERVISOR"]
     * }
     * </pre>
     * 
     * @return ResponseEntity con información del usuario autenticado o error 401
     */
    @GetMapping("/verify")
    @Operation(
            summary = "Verificar autenticación",
            description = "Verifica si hay un usuario autenticado en el contexto actual " +
                         "y retorna su información básica."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse (
            responseCode = "200",
            description = "Usuario autenticado - Retorna información del usuario"
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse (
            responseCode = "401",
            description = "No autenticado - Access token inválido o expirado"
    )
    public ResponseEntity<ApiResponse<Map<String, Object>>> verifyAuthentication() {
        try {
            // Obtener usuario del contexto de seguridad
            UserDetails currentUser = authenticationService.getCurrentUser();
            
            // Construir información del usuario
            Map<String, Object> userInfo = Map.of(
                    "authenticated", true,
                    "username", currentUser.getUsername(),
                    "authorities", currentUser.getAuthorities()
            );
            
            log.debug("Usuario autenticado verificado: {}", currentUser.getUsername());
            
            return ResponseEntity.ok(
                    ApiResponse.ok("Usuario autenticado", userInfo)
            );
            
        } catch (Exception ex) {
            log.warn("Verificación de autenticación fallida: {}", ex.getMessage());
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "No autenticado"));
        }
    }
}
