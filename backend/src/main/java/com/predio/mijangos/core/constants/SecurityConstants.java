package com.predio.mijangos.core.constants;

/**
 * Constantes relacionadas con la seguridad y autenticación del sistema.
 * 
 * <p>Define valores fijos para JWT, refresh tokens, y configuración de seguridad.
 * Estas constantes se utilizan en toda la aplicación para mantener
 * consistencia en la configuración de seguridad.
 * 
 * <p><b>Uso:</b>
 * <pre>
 * String header = SecurityConstants.JWT_HEADER;
 * int expiration = SecurityConstants.JWT_EXPIRATION_HOURS;
 * </pre>
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
public final class SecurityConstants {

    // ==================== JWT CONFIGURATION ====================

    /**
     * Nombre del header HTTP donde se envía el token JWT.
     * Valor: "Authorization"
     */
    public static final String JWT_HEADER = "Authorization";

    /**
     * Prefijo del token JWT en el header Authorization.
     * Valor: "Bearer "
     * 
     * <p>Ejemplo de uso: "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
     */
    public static final String JWT_TOKEN_PREFIX = "Bearer ";

    /**
     * Tiempo de expiración del access token JWT en horas.
     * Valor: 8 horas
     * 
     * <p>Después de este tiempo, el cliente debe usar el refresh token
     * para obtener un nuevo access token.
     */
    public static final int JWT_EXPIRATION_HOURS = 8;

    /**
     * Tiempo de expiración del access token JWT en milisegundos.
     * Calculado: 8 horas * 60 minutos * 60 segundos * 1000 milisegundos
     */
    public static final long JWT_EXPIRATION_MS = JWT_EXPIRATION_HOURS * 60 * 60 * 1000L;

    // ==================== REFRESH TOKEN CONFIGURATION ====================

    /**
     * Tiempo de expiración del refresh token en días.
     * Valor: 7 días
     * 
     * <p>Después de este tiempo, el usuario debe iniciar sesión nuevamente
     * con sus credenciales.
     */
    public static final int REFRESH_TOKEN_EXPIRATION_DAYS = 7;

    /**
     * Tiempo de expiración del refresh token en milisegundos.
     * Calculado: 7 días * 24 horas * 60 minutos * 60 segundos * 1000 milisegundos
     */
    public static final long REFRESH_TOKEN_EXPIRATION_MS = 
            REFRESH_TOKEN_EXPIRATION_DAYS * 24 * 60 * 60 * 1000L;

    // ==================== ROLES ====================

    /**
     * Rol de administrador del sistema.
     * Tiene acceso total a todas las funcionalidades.
     */
    public static final String ROLE_ADMIN = "ADMIN";

    /**
     * Rol de supervisor.
     * Tiene acceso a funciones de supervisión y reportes.
     */
    public static final String ROLE_SUPERVISOR = "SUPERVISOR";

    /**
     * Rol de personal de oficina.
     * Acceso a funciones administrativas básicas.
     */
    public static final String ROLE_OPERADOR = "OPERADOR";

    /**
     * Rol de vendedor.
     * Acceso a funciones de ventas y atención al cliente.
     */
    public static final String ROLE_VENDEDOR = "VENDEDOR";

    // ==================== ENDPOINTS PÚBLICOS ====================

    /**
     * Array de patrones de URLs que NO requieren autenticación.
     * 
     * <p>Incluye:
     * <ul>
     *   <li>/api/auth/** - Endpoints de autenticación (login, refresh, logout)</li>
     *   <li>/api/v3/api-docs/** - Documentación OpenAPI</li>
     *   <li>/swagger-ui/** - Interfaz Swagger UI</li>
     *   <li>/actuator/health - Health check público</li>
     * </ul>
     */
    public static final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/**",
            "/api/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/actuator/health",
            "/actuator/info"
    };

    // ==================== PASSWORD ====================

    /**
     * Longitud mínima de la contraseña.
     * Valor: 8 caracteres
     */
    public static final int PASSWORD_MIN_LENGTH = 8;

    /**
     * Longitud máxima de la contraseña.
     * Valor: 100 caracteres
     */
    public static final int PASSWORD_MAX_LENGTH = 100;

    /**
     * Expresión regular para validar contraseñas.
     * 
     * <p>Requiere:
     * <ul>
     *   <li>Al menos 8 caracteres</li>
     *   <li>Al menos una letra mayúscula</li>
     *   <li>Al menos una letra minúscula</li>
     *   <li>Al menos un dígito</li>
     * </ul>
     */
    public static final String PASSWORD_PATTERN = 
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";

    // ==================== CONSTRUCTOR PRIVADO ====================

    /**
     * Constructor privado para prevenir instanciación.
     * Esta es una clase de utilidad que solo contiene constantes estáticas.
     * 
     * @throws UnsupportedOperationException si se intenta instanciar
     */
    private SecurityConstants() {
        throw new UnsupportedOperationException(
                "SecurityConstants es una clase de utilidad y no debe ser instanciada"
        );
    }
}
