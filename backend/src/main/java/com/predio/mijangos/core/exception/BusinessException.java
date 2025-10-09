package com.predio.mijangos.core.exception;

import lombok.Getter;

/**
 * Excepción base para errores de lógica de negocio.
 * Todas las excepciones específicas de negocio deben extender de esta clase.
 * 
 * Proporciona un código de error específico para categorizar los diferentes
 * tipos de errores de negocio que pueden ocurrir en la aplicación.
 * 
 * Ejemplos de uso:
 * - throw new BusinessException("El producto está agotado");
 * - throw new BusinessException("STOCK_INSUFICIENTE", "No hay suficiente stock");
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * Código de error específico de la aplicación.
     * Permite identificar y categorizar el tipo de error de negocio.
     */
    private final String errorCode;

    /**
     * Constructor con mensaje simple.
     * Asigna un código de error genérico "BUSINESS_ERROR".
     * 
     * @param message Mensaje descriptivo del error
     */
    public BusinessException(String message) {
        super(message);
        this.errorCode = "BUSINESS_ERROR";
    }

    /**
     * Constructor con código de error personalizado y mensaje.
     * 
     * @param errorCode Código específico del error
     * @param message Mensaje descriptivo del error
     */
    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Constructor con mensaje y causa original.
     * 
     * @param message Mensaje descriptivo del error
     * @param cause Excepción original que causó este error
     */
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "BUSINESS_ERROR";
    }

    /**
     * Constructor completo con código de error, mensaje y causa.
     * 
     * @param errorCode Código específico del error
     * @param message Mensaje descriptivo del error
     * @param cause Excepción original que causó este error
     */
    public BusinessException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}