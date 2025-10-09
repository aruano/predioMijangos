package com.predio.mijangos.core.exception;

import com.predio.mijangos.core.response.ApiResponse;
import com.predio.mijangos.core.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para la aplicación.
 * Centraliza el manejo de todas las excepciones y proporciona respuestas
 * estandarizadas y consistentes para diferentes tipos de errores.
 * 
 * Intercepta excepciones en todos los @RestController y las convierte
 * en respuestas HTTP apropiadas con información estructurada del error.
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Maneja ResourceNotFoundException (404).
     * Se lanza cuando un recurso solicitado no existe.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        
        ErrorResponse error = ErrorResponse.of(
                404,
                ex.getErrorCode(),
                ex.getMessage(),
                request.getRequestURI()
        );
        
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(404, ex.getMessage(), error));
    }

    /**
     * Maneja BusinessException (400).
     * Se lanza cuando hay un error en la lógica de negocio.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleBusinessException(
            BusinessException ex, HttpServletRequest request) {
        
        log.error("Error de negocio: {} - {}", ex.getErrorCode(), ex.getMessage());
        
        ErrorResponse error = ErrorResponse.of(
                400,
                ex.getErrorCode(),
                ex.getMessage(),
                request.getRequestURI()
        );
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, ex.getMessage(), error));
    }

    /**
     * Maneja ValidationException (400).
     * Se lanza cuando la validación personalizada falla.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleValidationException(
            ValidationException ex, HttpServletRequest request) {
        
        log.warn("Error de validación: {}", ex.getMessage());
        
        ErrorResponse error = ErrorResponse.of(
                400,
                ex.getErrorCode(),
                ex.getMessage(),
                ex.getValidationErrors(),
                request.getRequestURI()
        );
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, ex.getMessage(), error));
    }

    /**
     * Maneja errores de validación de Bean Validation (400).
     * Se lanza automáticamente cuando @Valid falla en un @RequestBody.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        log.warn("Errores de validación: {}", errors);
        
        ErrorResponse error = ErrorResponse.of(
                400,
                "VALIDATION_ERROR",
                "Errores de validación en los campos proporcionados",
                errors,
                request.getRequestURI()
        );
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, "Errores de validación", error));
    }

    /**
     * Maneja errores de tipo de argumento incorrecto (400).
     * Se lanza cuando un parámetro de URL o query tiene un tipo inválido.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        
        String message = String.format("El parámetro '%s' debe ser de tipo %s", 
                ex.getName(), 
                ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "desconocido");
        
        log.warn("Error de tipo de argumento: {}", message);
        
        ErrorResponse error = ErrorResponse.of(
                400,
                "INVALID_PARAMETER_TYPE",
                message,
                request.getRequestURI()
        );
        
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(400, message, error));
    }

    /**
     * Maneja violaciones de integridad de datos (409).
     * Se lanza cuando hay conflictos de unique constraints, foreign keys, etc.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        
        log.error("Violación de integridad de datos: {}", ex.getMessage());
        
        String message = "Error de integridad de datos";
        String errorCode = "DATA_INTEGRITY_VIOLATION";
        
        // Personalizar mensaje según el tipo de violación
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("Duplicate entry")) {
                message = "El registro ya existe en el sistema";
                errorCode = "DUPLICATE_ENTRY";
            } else if (ex.getMessage().contains("foreign key constraint")) {
                message = "No se puede eliminar el registro porque tiene datos relacionados";
                errorCode = "FOREIGN_KEY_VIOLATION";
            } else if (ex.getMessage().contains("cannot be null")) {
                message = "Campos requeridos no pueden estar vacíos";
                errorCode = "NULL_CONSTRAINT_VIOLATION";
            }
        }
        
        ErrorResponse error = ErrorResponse.of(
                409,
                errorCode,
                message,
                request.getRequestURI()
        );
        
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(409, message, error));
    }

    /**
     * Maneja errores de autenticación (401).
     * Se lanza cuando las credenciales son inválidas.
     */
    @ExceptionHandler({AuthenticationException.class, BadCredentialsException.class})
    public ResponseEntity<ApiResponse<ErrorResponse>> handleAuthenticationException(
            Exception ex, HttpServletRequest request) {
        
        log.warn("Error de autenticación: {}", ex.getMessage());
        
        ErrorResponse error = ErrorResponse.of(
                401,
                "AUTHENTICATION_FAILED",
                "Credenciales inválidas",
                request.getRequestURI()
        );
        
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, "Credenciales inválidas", error));
    }

    /**
     * Maneja errores de autorización (403).
     * Se lanza cuando el usuario no tiene permisos suficientes.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleAccessDenied(
            AccessDeniedException ex, HttpServletRequest request) {
        
        log.warn("Acceso denegado: {}", ex.getMessage());
        
        ErrorResponse error = ErrorResponse.of(
                403,
                "ACCESS_DENIED",
                "No tienes permisos para acceder a este recurso",
                request.getRequestURI()
        );
        
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(403, "Acceso denegado", error));
    }

    /**
     * Maneja cualquier otra excepción no contemplada (500).
     * Último recurso para errores inesperados.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        log.error("Error inesperado", ex);
        
        ErrorResponse error = ErrorResponse.of(
                500,
                "INTERNAL_SERVER_ERROR",
                "Ha ocurrido un error interno. Por favor contacta al administrador.",
                request.getRequestURI()
        );
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Error interno del servidor", error));
    }
}