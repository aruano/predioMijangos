package com.predio.mijangos.core.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Excepción lanzada cuando la validación de datos falla.
 * Resulta en una respuesta HTTP 400 (Bad Request).
 * 
 * Permite incluir múltiples errores de validación asociados a campos específicos,
 * proporcionando feedback detallado al cliente sobre qué datos son inválidos.
 * 
 * Ejemplos de uso:
 * - throw new ValidationException("El nombre es requerido");
 * - throw new ValidationException(errorsMap);
 * - throw new ValidationException("INVALID_FORMAT", "El formato del DPI es incorrecto");
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
@Getter
public class ValidationException extends BusinessException {

    /**
     * Mapa de errores de validación.
     * Clave: nombre del campo, Valor: mensaje de error del campo.
     */
    private final Map<String, String> validationErrors;

    /**
     * Constructor con mensaje simple.
     * 
     * @param message Mensaje descriptivo del error de validación
     */
    public ValidationException(String message) {
        super("VALIDATION_ERROR", message);
        this.validationErrors = new HashMap<>();
    }

    /**
     * Constructor con código de error personalizado y mensaje.
     * 
     * @param errorCode Código específico del error de validación
     * @param message Mensaje descriptivo del error
     */
    public ValidationException(String errorCode, String message) {
        super(errorCode, message);
        this.validationErrors = new HashMap<>();
    }

    /**
     * Constructor con mapa de errores de validación.
     * Útil cuando hay múltiples campos con errores.
     * 
     * @param validationErrors Mapa con los errores (campo -> mensaje)
     */
    public ValidationException(Map<String, String> validationErrors) {
        super("VALIDATION_ERROR", "Errores de validación en los campos proporcionados");
        this.validationErrors = validationErrors != null ? validationErrors : new HashMap<>();
    }

    /**
     * Constructor con mensaje y mapa de errores.
     * 
     * @param message Mensaje general del error de validación
     * @param validationErrors Mapa con los errores específicos por campo
     */
    public ValidationException(String message, Map<String, String> validationErrors) {
        super("VALIDATION_ERROR", message);
        this.validationErrors = validationErrors != null ? validationErrors : new HashMap<>();
    }

    /**
     * Agrega un error de validación para un campo específico.
     * 
     * @param fieldName Nombre del campo con error
     * @param errorMessage Mensaje de error del campo
     */
    public void addValidationError(String fieldName, String errorMessage) {
        this.validationErrors.put(fieldName, errorMessage);
    }

    /**
     * Verifica si hay errores de validación registrados.
     * 
     * @return true si hay al menos un error de validación, false en caso contrario
     */
    public boolean hasValidationErrors() {
        return !this.validationErrors.isEmpty();
    }
}