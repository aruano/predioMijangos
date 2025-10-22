package com.predio.mijangos.core.constants;

/**
 * Códigos de error estandarizados para la aplicación.
 * 
 * <p>Define códigos únicos para cada tipo de error que puede ocurrir
 * en el sistema. Estos códigos se utilizan en las excepciones custom
 * y en las respuestas de error de la API.
 * 
 * <p><b>Formato de códigos:</b>
 * <ul>
 *   <li>CATEGORÍA_DESCRIPCIÓN</li>
 *   <li>Ejemplo: RESOURCE_NOT_FOUND, VALIDATION_ERROR, etc.</li>
 * </ul>
 * 
 * <p><b>Uso:</b>
 * <pre>
 * throw new ResourceNotFoundException(ErrorCodes.RESOURCE_NOT_FOUND, "Usuario no encontrado");
 * </pre>
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
public final class ErrorCodes {

    // ==================== ERRORES GENERALES (1xxx) ====================

    /**
     * Error interno del servidor.
     * HTTP Status: 500
     */
    public static final String INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR";

    /**
     * Error no manejado o desconocido.
     * HTTP Status: 500
     */
    public static final String UNKNOWN_ERROR = "UNKNOWN_ERROR";

    // ==================== ERRORES DE RECURSOS (2xxx) ====================

    /**
     * Recurso solicitado no encontrado.
     * HTTP Status: 404
     */
    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";

    /**
     * Usuario no encontrado.
     * HTTP Status: 404
     */
    public static final String USUARIO_NOT_FOUND = "USUARIO_NOT_FOUND";

    /**
     * Cliente no encontrado.
     * HTTP Status: 404
     */
    public static final String CLIENTE_NOT_FOUND = "CLIENTE_NOT_FOUND";

    /**
     * Persona no encontrada.
     * HTTP Status: 404
     */
    public static final String PERSONA_NOT_FOUND = "PERSONA_NOT_FOUND";

    /**
     * Rol no encontrado.
     * HTTP Status: 404
     */
    public static final String ROL_NOT_FOUND = "ROL_NOT_FOUND";

    /**
     * Producto no encontrado.
     * HTTP Status: 404
     */
    public static final String PRODUCTO_NOT_FOUND = "PRODUCTO_NOT_FOUND";

    // ==================== ERRORES DE VALIDACIÓN (3xxx) ====================

    /**
     * Error de validación genérico.
     * HTTP Status: 400
     */
    public static final String VALIDATION_ERROR = "VALIDATION_ERROR";

    /**
     * Campos requeridos están vacíos o nulos.
     * HTTP Status: 400
     */
    public static final String REQUIRED_FIELD_MISSING = "REQUIRED_FIELD_MISSING";

    /**
     * Formato de campo inválido.
     * HTTP Status: 400
     */
    public static final String INVALID_FORMAT = "INVALID_FORMAT";

    /**
     * Valor fuera del rango permitido.
     * HTTP Status: 400
     */
    public static final String VALUE_OUT_OF_RANGE = "VALUE_OUT_OF_RANGE";

    // ==================== ERRORES DE NEGOCIO (4xxx) ====================

    /**
     * Error de lógica de negocio genérico.
     * HTTP Status: 400
     */
    public static final String BUSINESS_ERROR = "BUSINESS_ERROR";

    /**
     * Registro duplicado (viola unique constraint).
     * HTTP Status: 409
     */
    public static final String DUPLICATE_ENTRY = "DUPLICATE_ENTRY";

    /**
     * Usuario con código de empleado duplicado.
     * HTTP Status: 409
     */
    public static final String USUARIO_DUPLICADO = "USUARIO_DUPLICADO";

    /**
     * Cliente con identificación duplicada.
     * HTTP Status: 409
     */
    public static final String CLIENTE_DUPLICADO = "CLIENTE_DUPLICADO";

    /**
     * Persona con identificación duplicada.
     * HTTP Status: 409
     */
    public static final String PERSONA_DUPLICADA = "PERSONA_DUPLICADA";

    /**
     * Stock insuficiente para completar la operación.
     * HTTP Status: 400
     */
    public static final String STOCK_INSUFICIENTE = "STOCK_INSUFICIENTE";

    /**
     * Operación no permitida en el estado actual del recurso.
     * HTTP Status: 400
     */
    public static final String INVALID_STATE = "INVALID_STATE";

    // ==================== ERRORES DE INTEGRIDAD (5xxx) ====================

    /**
     * Violación de integridad de datos genérica.
     * HTTP Status: 409
     */
    public static final String DATA_INTEGRITY_VIOLATION = "DATA_INTEGRITY_VIOLATION";

    /**
     * Violación de foreign key constraint.
     * HTTP Status: 409
     */
    public static final String FOREIGN_KEY_VIOLATION = "FOREIGN_KEY_VIOLATION";

    /**
     * Violación de constraint NOT NULL.
     * HTTP Status: 400
     */
    public static final String NULL_CONSTRAINT_VIOLATION = "NULL_CONSTRAINT_VIOLATION";

    /**
     * No se puede eliminar el recurso porque tiene datos relacionados.
     * HTTP Status: 409
     */
    public static final String HAS_DEPENDENCIES = "HAS_DEPENDENCIES";

    // ==================== ERRORES DE AUTENTICACIÓN (6xxx) ====================

    /**
     * Error de autenticación genérico.
     * HTTP Status: 401
     */
    public static final String AUTHENTICATION_FAILED = "AUTHENTICATION_FAILED";

    /**
     * Credenciales inválidas (usuario o contraseña incorrectos).
     * HTTP Status: 401
     */
    public static final String INVALID_CREDENTIALS = "INVALID_CREDENTIALS";

    /**
     * Token JWT inválido o malformado.
     * HTTP Status: 401
     */
    public static final String INVALID_TOKEN = "INVALID_TOKEN";

    /**
     * Token JWT expirado.
     * HTTP Status: 401
     */
    public static final String TOKEN_EXPIRED = "TOKEN_EXPIRED";

    /**
     * Refresh token inválido o expirado.
     * HTTP Status: 401
     */
    public static final String INVALID_REFRESH_TOKEN = "INVALID_REFRESH_TOKEN";

    /**
     * Usuario inactivo o deshabilitado.
     * HTTP Status: 401
     */
    public static final String USER_DISABLED = "USER_DISABLED";

    /**
     * Cuenta de usuario bloqueada.
     * HTTP Status: 401
     */
    public static final String ACCOUNT_LOCKED = "ACCOUNT_LOCKED";

    // ==================== ERRORES DE AUTORIZACIÓN (7xxx) ====================

    /**
     * Acceso denegado (usuario sin permisos suficientes).
     * HTTP Status: 403
     */
    public static final String ACCESS_DENIED = "ACCESS_DENIED";

    /**
     * Rol insuficiente para realizar la operación.
     * HTTP Status: 403
     */
    public static final String INSUFFICIENT_ROLE = "INSUFFICIENT_ROLE";

    /**
     * Permiso específico no encontrado.
     * HTTP Status: 403
     */
    public static final String PERMISSION_DENIED = "PERMISSION_DENIED";

    // ==================== ERRORES DE PARÁMETROS (8xxx) ====================

    /**
     * Tipo de parámetro inválido.
     * HTTP Status: 400
     */
    public static final String INVALID_PARAMETER_TYPE = "INVALID_PARAMETER_TYPE";

    /**
     * Parámetro requerido faltante.
     * HTTP Status: 400
     */
    public static final String MISSING_PARAMETER = "MISSING_PARAMETER";

    /**
     * Valor de parámetro inválido.
     * HTTP Status: 400
     */
    public static final String INVALID_PARAMETER_VALUE = "INVALID_PARAMETER_VALUE";

    // ==================== CONSTRUCTOR PRIVADO ====================

    /**
     * Constructor privado para prevenir instanciación.
     * Esta es una clase de utilidad que solo contiene constantes estáticas.
     * 
     * @throws UnsupportedOperationException si se intenta instanciar
     */
    private ErrorCodes() {
        throw new UnsupportedOperationException(
                "ErrorCodes es una clase de utilidad y no debe ser instanciada"
        );
    }
}
