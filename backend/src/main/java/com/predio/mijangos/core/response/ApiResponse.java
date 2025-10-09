package com.predio.mijangos.core.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Respuesta estándar para todos los endpoints de la API.
 * Proporciona una estructura consistente para las respuestas HTTP.
 * 
 * Estructura de respuesta:
 * {
 *   "statusCode": 200,
 *   "message": "Success",
 *   "body": { ... },
 *   "timestamp": "2025-10-02T10:30:00"
 * }
 * 
 * @param <T> Tipo de datos del cuerpo de la respuesta
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /**
     * Código de estado HTTP de la respuesta.
     */
    private Integer statusCode;

    /**
     * Mensaje descriptivo de la respuesta.
     */
    private String message;

    /**
     * Cuerpo de la respuesta con los datos solicitados.
     * Puede ser null en caso de errores o respuestas sin contenido.
     */
    private T body;

    /**
     * Timestamp de cuando se generó la respuesta.
     */
    private LocalDateTime timestamp;

    // ==================== FACTORY METHODS ====================

    /**
     * Crea una respuesta exitosa (200 OK) con datos.
     * 
     * @param data Datos a incluir en el cuerpo de la respuesta
     * @param <T> Tipo de los datos
     * @return ApiResponse con status 200 y los datos proporcionados
     */
    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.<T>builder()
                .statusCode(200)
                .message("Success")
                .body(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Crea una respuesta exitosa (200 OK) con mensaje personalizado y datos.
     * 
     * @param message Mensaje personalizado de éxito
     * @param data Datos a incluir en el cuerpo
     * @param <T> Tipo de los datos
     * @return ApiResponse con status 200, mensaje personalizado y datos
     */
    public static <T> ApiResponse<T> ok(String message, T data) {
        return ApiResponse.<T>builder()
                .statusCode(200)
                .message(message)
                .body(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Crea una respuesta de recurso creado (201 CREATED).
     * 
     * @param message Mensaje descriptivo de la creación
     * @param data Datos del recurso creado
     * @param <T> Tipo de los datos
     * @return ApiResponse con status 201 y los datos del recurso creado
     */
    public static <T> ApiResponse<T> created(String message, T data) {
        return ApiResponse.<T>builder()
                .statusCode(201)
                .message(message)
                .body(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Crea una respuesta sin contenido (204 NO CONTENT).
     * Se usa típicamente para operaciones DELETE exitosas.
     * 
     * @param message Mensaje descriptivo de la operación
     * @param <T> Tipo de los datos (generalmente Void)
     * @return ApiResponse con status 204 sin datos en el cuerpo
     */
    public static <T> ApiResponse<T> noContent(String message) {
        return ApiResponse.<T>builder()
                .statusCode(204)
                .message(message)
                .body(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Crea una respuesta de error genérica.
     * 
     * @param statusCode Código de estado HTTP del error
     * @param message Mensaje descriptivo del error
     * @param <T> Tipo de los datos (generalmente Void)
     * @return ApiResponse con el código de error y mensaje especificados
     */
    public static <T> ApiResponse<T> error(Integer statusCode, String message) {
        return ApiResponse.<T>builder()
                .statusCode(statusCode)
                .message(message)
                .body(null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Crea una respuesta de error con datos adicionales.
     * Útil para enviar detalles de validación o información contextual del error.
     * 
     * @param statusCode Código de estado HTTP del error
     * @param message Mensaje descriptivo del error
     * @param errorData Datos adicionales del error (ej: errores de validación)
     * @param <T> Tipo de los datos de error
     * @return ApiResponse con el código de error, mensaje y datos adicionales
     */
    public static <T> ApiResponse<T> error(Integer statusCode, String message, T errorData) {
        return ApiResponse.<T>builder()
                .statusCode(statusCode)
                .message(message)
                .body(errorData)
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Crea una respuesta de solicitud incorrecta (400 BAD REQUEST).
     * 
     * @param message Mensaje descriptivo del error
     * @return ApiResponse con status 400
     */
    public static ApiResponse<Void> badRequest(String message) {
        return error(400, message);
    }

    /**
     * Crea una respuesta de no autorizado (401 UNAUTHORIZED).
     * 
     * @param message Mensaje descriptivo del error de autenticación
     * @return ApiResponse con status 401
     */
    public static ApiResponse<Void> unauthorized(String message) {
        return error(401, message);
    }

    /**
     * Crea una respuesta de prohibido (403 FORBIDDEN).
     * 
     * @param message Mensaje descriptivo del error de autorización
     * @return ApiResponse con status 403
     */
    public static ApiResponse<Void> forbidden(String message) {
        return error(403, message);
    }

    /**
     * Crea una respuesta de no encontrado (404 NOT FOUND).
     * 
     * @param message Mensaje descriptivo del recurso no encontrado
     * @return ApiResponse con status 404
     */
    public static ApiResponse<Void> notFound(String message) {
        return error(404, message);
    }

    /**
     * Crea una respuesta de conflicto (409 CONFLICT).
     * 
     * @param message Mensaje descriptivo del conflicto
     * @return ApiResponse con status 409
     */
    public static ApiResponse<Void> conflict(String message) {
        return error(409, message);
    }

    /**
     * Crea una respuesta de error interno del servidor (500 INTERNAL SERVER ERROR).
     * 
     * @param message Mensaje descriptivo del error interno
     * @return ApiResponse con status 500
     */
    public static ApiResponse<Void> internalServerError(String message) {
        return error(500, message);
    }
}