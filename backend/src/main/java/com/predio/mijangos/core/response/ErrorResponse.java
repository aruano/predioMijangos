package com.predio.mijangos.core.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Respuesta de error detallada para manejo de excepciones.
 * Proporciona información estructurada sobre errores ocurridos en la API.
 * 
 * Estructura de respuesta:
 * {
 *   "statusCode": 400,
 *   "errorCode": "VALIDATION_ERROR",
 *   "message": "Errores de validación en los campos",
 *   "details": { "campo": "mensaje de error" },
 *   "path": "/api/productos",
 *   "timestamp": "2025-10-02T10:30:00"
 * }
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * Código de estado HTTP del error.
     */
    private Integer statusCode;

    /**
     * Código de error específico de la aplicación.
     * Permite identificar el tipo exacto de error (ej: "VALIDATION_ERROR", "RESOURCE_NOT_FOUND").
     */
    private String errorCode;

    /**
     * Mensaje descriptivo del error en lenguaje natural.
     */
    private String message;

    /**
     * Detalles adicionales del error.
     * Útil para errores de validación donde se incluyen múltiples campos con errores.
     */
    private Map<String, String> details;

    /**
     * Ruta del endpoint donde ocurrió el error.
     */
    private String path;

    /**
     * Timestamp de cuando ocurrió el error.
     */
    private LocalDateTime timestamp;

    /**
     * Información de debugging (solo en desarrollo).
     * Puede incluir stack trace u otra información técnica.
     */
    private String debugInfo;

    /**
     * Crea una respuesta de error básica.
     * 
     * @param statusCode Código HTTP del error
     * @param errorCode Código de error de la aplicación
     * @param message Mensaje descriptivo
     * @param path Ruta donde ocurrió el error
     * @return ErrorResponse construido
     */
    public static ErrorResponse of(Integer statusCode, String errorCode, String message, String path) {
        return ErrorResponse.builder()
                .statusCode(statusCode)
                .errorCode(errorCode)
                .message(message)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Crea una respuesta de error con detalles adicionales.
     * Usado principalmente para errores de validación.
     * 
     * @param statusCode Código HTTP del error
     * @param errorCode Código de error de la aplicación
     * @param message Mensaje descriptivo
     * @param details Mapa con detalles del error (campo -> mensaje)
     * @param path Ruta donde ocurrió el error
     * @return ErrorResponse construido con detalles
     */
    public static ErrorResponse of(Integer statusCode, String errorCode, String message, 
                                   Map<String, String> details, String path) {
        return ErrorResponse.builder()
                .statusCode(statusCode)
                .errorCode(errorCode)
                .message(message)
                .details(details)
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Crea una respuesta de error con información de debugging.
     * Solo debe usarse en ambientes de desarrollo/testing.
     * 
     * @param statusCode Código HTTP del error
     * @param errorCode Código de error de la aplicación
     * @param message Mensaje descriptivo
     * @param path Ruta donde ocurrió el error
     * @param debugInfo Información de debugging (stack trace, etc)
     * @return ErrorResponse construido con info de debugging
     */
    public static ErrorResponse ofWithDebug(Integer statusCode, String errorCode, String message, 
                                            String path, String debugInfo) {
        return ErrorResponse.builder()
                .statusCode(statusCode)
                .errorCode(errorCode)
                .message(message)
                .path(path)
                .debugInfo(debugInfo)
                .timestamp(LocalDateTime.now())
                .build();
    }
}