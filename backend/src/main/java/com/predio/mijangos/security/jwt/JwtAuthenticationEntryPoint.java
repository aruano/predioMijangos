package com.predio.mijangos.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.predio.mijangos.core.response.ApiResponse;
import com.predio.mijangos.core.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Punto de entrada personalizado para manejar errores de autenticación.
 * 
 * Se invoca automáticamente cuando:
 * - Un usuario intenta acceder a un recurso protegido sin autenticarse
 * - Un token JWT es inválido o ha expirado
 * - Ocurre cualquier error durante el proceso de autenticación
 * 
 * Proporciona respuestas JSON consistentes en lugar del comportamiento
 * por defecto de Spring Security (redirección a página de login).
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Maneja errores de autenticación y genera una respuesta JSON apropiada.
     * 
     * Siempre devuelve HTTP 401 (Unauthorized) con un mensaje descriptivo
     * en formato JSON usando el estándar ApiResponse.
     * 
     * @param request Request HTTP que causó el error
     * @param response Response HTTP donde se escribirá el error
     * @param authException Excepción de autenticación que ocurrió
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        
        log.warn("Error de autenticación en {}: {}", 
                request.getRequestURI(), 
                authException.getMessage());

        // Determinar el mensaje de error apropiado
        String errorMessage = determineErrorMessage(request, authException);
        String errorCode = determineErrorCode(request, authException);
        
        // Crear respuesta de error estructurada
        ErrorResponse errorResponse = ErrorResponse.of(
                401,
                errorCode,
                errorMessage,
                request.getRequestURI()
        );
        
        ApiResponse<ErrorResponse> apiResponse = ApiResponse.error(
                401,
                errorMessage,
                errorResponse
        );
        
        // Configurar respuesta HTTP
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        // Escribir JSON en el response
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.getWriter().flush();
    }

    /**
     * Determina el mensaje de error apropiado basado en la excepción y el request.
     * 
     * @param request Request HTTP
     * @param authException Excepción de autenticación
     * @return Mensaje de error descriptivo
     */
    private String determineErrorMessage(HttpServletRequest request, AuthenticationException authException) {
        String authHeader = request.getHeader("Authorization");
        
        // Sin header de autorización
        if (authHeader == null || authHeader.isEmpty()) {
            return "Autenticación requerida. Por favor proporciona un token válido.";
        }
        
        // Header mal formado
        if (!authHeader.startsWith("Bearer ")) {
            return "Formato de autorización inválido. Usa: Bearer {token}";
        }
        
        // Token probablemente inválido o expirado
        String exceptionMessage = authException.getMessage();
        if (exceptionMessage != null) {
            if (exceptionMessage.contains("expired")) {
                return "Token expirado. Por favor, inicia sesión nuevamente.";
            }
            if (exceptionMessage.contains("invalid")) {
                return "Token inválido. Por favor, verifica tus credenciales.";
            }
            if (exceptionMessage.contains("malformed")) {
                return "Token malformado. El formato del token es incorrecto.";
            }
        }
        
        // Error genérico de autenticación
        return "Error de autenticación. Por favor, verifica tus credenciales.";
    }

    /**
     * Determina el código de error apropiado.
     * 
     * @param request Request HTTP
     * @param authException Excepción de autenticación
     * @return Código de error
     */
    private String determineErrorCode(HttpServletRequest request, AuthenticationException authException) {
        String authHeader = request.getHeader("Authorization");
        
        if (authHeader == null || authHeader.isEmpty()) {
            return "AUTHENTICATION_REQUIRED";
        }
        
        if (!authHeader.startsWith("Bearer ")) {
            return "INVALID_AUTH_FORMAT";
        }
        
        String exceptionMessage = authException.getMessage();
        if (exceptionMessage != null) {
            if (exceptionMessage.contains("expired")) {
                return "TOKEN_EXPIRED";
            }
            if (exceptionMessage.contains("invalid")) {
                return "TOKEN_INVALID";
            }
            if (exceptionMessage.contains("malformed")) {
                return "TOKEN_MALFORMED";
            }
        }
        
        return "AUTHENTICATION_FAILED";
    }
}