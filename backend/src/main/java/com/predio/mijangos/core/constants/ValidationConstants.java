package com.predio.mijangos.core.constants;

/**
 * Constantes para validación de datos en la aplicación.
 * 
 * <p>Define valores de validación como longitudes mínimas/máximas,
 * patrones regex, y mensajes de error estándar para validaciones.
 * 
 * <p>Estas constantes se usan principalmente en:
 * <ul>
 *   <li>Anotaciones de validación en DTOs (@Size, @Pattern, etc.)</li>
 *   <li>Validadores custom</li>
 *   <li>Validaciones de negocio en servicios</li>
 * </ul>
 * 
 * <p><b>Uso en DTOs:</b>
 * <pre>
 * public record UsuarioCreateDTO(
 *     {@literal @}Size(
 *         min = ValidationConstants.USUARIO_MIN_LENGTH,
 *         max = ValidationConstants.USUARIO_MAX_LENGTH,
 *         message = ValidationConstants.USUARIO_SIZE_MESSAGE
 *     )
 *     String usuario
 * ) {}
 * </pre>
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
public final class ValidationConstants {

    // ==================== USUARIO ====================

    /**
     * Longitud mínima del código de usuario.
     * Valor: 3 caracteres
     */
    public static final int USUARIO_MIN_LENGTH = 3;

    /**
     * Longitud máxima del código de usuario.
     * Valor: 100 caracteres
     */
    public static final int USUARIO_MAX_LENGTH = 100;

    /**
     * Mensaje de error para validación de tamaño de usuario.
     */
    public static final String USUARIO_SIZE_MESSAGE = 
            "El código de usuario debe tener entre " + USUARIO_MIN_LENGTH + 
            " y " + USUARIO_MAX_LENGTH + " caracteres";

    /**
     * Mensaje de error cuando el usuario es requerido pero está vacío.
     */
    public static final String USUARIO_REQUIRED_MESSAGE = "El código de usuario es obligatorio";

    // ==================== CONTRASEÑA ====================

    /**
     * Longitud mínima de contraseña.
     * Valor: 8 caracteres
     */
    public static final int PASSWORD_MIN_LENGTH = 8;

    /**
     * Longitud máxima de contraseña.
     * Valor: 100 caracteres
     */
    public static final int PASSWORD_MAX_LENGTH = 100;

    /**
     * Patrón regex para validar contraseñas fuertes.
     * Requiere: mayúscula, minúscula, dígito, y al menos 8 caracteres.
     */
    public static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";

    /**
     * Mensaje de error para validación de contraseña.
     */
    public static final String PASSWORD_MESSAGE = 
            "La contraseña debe tener al menos 8 caracteres, incluir mayúsculas, " +
            "minúsculas y números";

    // ==================== NOMBRES Y APELLIDOS ====================

    /**
     * Longitud mínima para nombres.
     * Valor: 2 caracteres
     */
    public static final int NOMBRE_MIN_LENGTH = 2;

    /**
     * Longitud máxima para nombres.
     * Valor: 100 caracteres
     */
    public static final int NOMBRE_MAX_LENGTH = 100;

    /**
     * Mensaje de error para validación de tamaño de nombre.
     */
    public static final String NOMBRE_SIZE_MESSAGE = 
            "El nombre debe tener entre " + NOMBRE_MIN_LENGTH + 
            " y " + NOMBRE_MAX_LENGTH + " caracteres";

    /**
     * Longitud mínima para apellidos.
     * Valor: 2 caracteres
     */
    public static final int APELLIDO_MIN_LENGTH = 2;

    /**
     * Longitud máxima para apellidos.
     * Valor: 100 caracteres
     */
    public static final int APELLIDO_MAX_LENGTH = 100;

    /**
     * Mensaje de error para validación de tamaño de apellido.
     */
    public static final String APELLIDO_SIZE_MESSAGE = 
            "El apellido debe tener entre " + APELLIDO_MIN_LENGTH + 
            " y " + APELLIDO_MAX_LENGTH + " caracteres";

    // ==================== IDENTIFICACIÓN ====================

    /**
     * Longitud mínima de identificación (DPI, NIT, etc.).
     * Valor: 8 caracteres
     */
    public static final int IDENTIFICACION_MIN_LENGTH = 8;

    /**
     * Longitud máxima de identificación.
     * Valor: 20 caracteres
     */
    public static final int IDENTIFICACION_MAX_LENGTH = 20;

    /**
     * Mensaje de error para validación de identificación.
     */
    public static final String IDENTIFICACION_SIZE_MESSAGE = 
            "La identificación debe tener entre " + IDENTIFICACION_MIN_LENGTH + 
            " y " + IDENTIFICACION_MAX_LENGTH + " caracteres";

    /**
     * Patrón para DPI guatemalteco.
     * Formato: 13 dígitos sin espacios ni guiones
     */
    public static final String DPI_PATTERN = "^[0-9]{13}$";

    /**
     * Mensaje de error para DPI inválido.
     */
    public static final String DPI_MESSAGE = "El DPI debe contener exactamente 13 dígitos";

    /**
     * Patrón para NIT guatemalteco.
     * Formato: 7-8 dígitos seguidos de un guión y un dígito verificador
     * Ejemplos: 1234567-8, 12345678-9
     */
    public static final String NIT_PATTERN = "^[0-9]{7,8}-[0-9K]$";

    /**
     * Mensaje de error para NIT inválido.
     */
    public static final String NIT_MESSAGE = 
            "El NIT debe tener el formato correcto (ej: 1234567-8 o 12345678-9)";

    // ==================== TELÉFONO ====================

    /**
     * Longitud mínima de teléfono.
     * Valor: 8 caracteres
     */
    public static final int TELEFONO_MIN_LENGTH = 8;

    /**
     * Longitud máxima de teléfono.
     * Valor: 20 caracteres
     */
    public static final int TELEFONO_MAX_LENGTH = 20;

    /**
     * Patrón para teléfono guatemalteco (móvil y fijo).
     * Formatos aceptados:
     * - 8 dígitos: 12345678
     * - Con código país: +50212345678
     * - Con guiones: 1234-5678
     */
    public static final String TELEFONO_PATTERN = 
            "^(\\+502)?[0-9]{8}$|^[0-9]{4}-[0-9]{4}$";

    /**
     * Mensaje de error para teléfono inválido.
     */
    public static final String TELEFONO_MESSAGE = 
            "El teléfono debe tener formato válido (8 dígitos, ej: 12345678)";

    // ==================== EMAIL ====================

    /**
     * Longitud máxima de email.
     * Valor: 255 caracteres
     */
    public static final int EMAIL_MAX_LENGTH = 255;

    /**
     * Patrón regex para validar email.
     * Más permisivo que el estándar pero funcional para casos comunes.
     */
    public static final String EMAIL_PATTERN = 
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    /**
     * Mensaje de error para email inválido.
     */
    public static final String EMAIL_MESSAGE = "El correo electrónico no tiene un formato válido";

    // ==================== DIRECCIÓN ====================

    /**
     * Longitud mínima de dirección.
     * Valor: 10 caracteres
     */
    public static final int DIRECCION_MIN_LENGTH = 10;

    /**
     * Longitud máxima de dirección.
     * Valor: 500 caracteres
     */
    public static final int DIRECCION_MAX_LENGTH = 500;

    /**
     * Mensaje de error para validación de dirección.
     */
    public static final String DIRECCION_SIZE_MESSAGE = 
            "La dirección debe tener entre " + DIRECCION_MIN_LENGTH + 
            " y " + DIRECCION_MAX_LENGTH + " caracteres";

    // ==================== DESCRIPCIÓN ====================

    /**
     * Longitud máxima de descripción.
     * Valor: 1000 caracteres
     */
    public static final int DESCRIPCION_MAX_LENGTH = 1000;

    /**
     * Mensaje de error para descripción muy larga.
     */
    public static final String DESCRIPCION_SIZE_MESSAGE = 
            "La descripción no puede exceder " + DESCRIPCION_MAX_LENGTH + " caracteres";

    // ==================== CÓDIGO/SKU ====================

    /**
     * Longitud mínima de código/SKU.
     * Valor: 3 caracteres
     */
    public static final int CODIGO_MIN_LENGTH = 3;

    /**
     * Longitud máxima de código/SKU.
     * Valor: 50 caracteres
     */
    public static final int CODIGO_MAX_LENGTH = 50;

    /**
     * Patrón para código/SKU alfanumérico.
     * Permite letras, números, guiones y guiones bajos.
     */
    public static final String CODIGO_PATTERN = "^[A-Za-z0-9_-]+$";

    /**
     * Mensaje de error para código inválido.
     */
    public static final String CODIGO_MESSAGE = 
            "El código debe contener solo letras, números, guiones y guiones bajos";

    // ==================== VALORES NUMÉRICOS ====================

    /**
     * Valor mínimo para cantidades.
     * Valor: 0
     */
    public static final int CANTIDAD_MIN = 0;

    /**
     * Valor máximo para cantidades.
     * Valor: 999999
     */
    public static final int CANTIDAD_MAX = 999999;

    /**
     * Mensaje de error para cantidad inválida.
     */
    public static final String CANTIDAD_RANGE_MESSAGE = 
            "La cantidad debe estar entre " + CANTIDAD_MIN + " y " + CANTIDAD_MAX;

    /**
     * Valor mínimo para precios.
     * Valor: 0.01 (un centavo)
     */
    public static final String PRECIO_MIN = "0.01";

    /**
     * Valor máximo para precios.
     * Valor: 9999999.99 (casi 10 millones)
     */
    public static final String PRECIO_MAX = "9999999.99";

    /**
     * Mensaje de error para precio inválido.
     */
    public static final String PRECIO_RANGE_MESSAGE = 
            "El precio debe estar entre " + PRECIO_MIN + " y " + PRECIO_MAX;

    // ==================== PAGINACIÓN ====================

    /**
     * Tamaño mínimo de página.
     * Valor: 1
     */
    public static final int PAGE_SIZE_MIN = 1;

    /**
     * Tamaño máximo de página.
     * Valor: 100
     */
    public static final int PAGE_SIZE_MAX = 100;

    /**
     * Tamaño de página por defecto.
     * Valor: 20
     */
    public static final int PAGE_SIZE_DEFAULT = 20;

    // ==================== MENSAJES GENÉRICOS ====================

    /**
     * Mensaje genérico para campo requerido.
     */
    public static final String FIELD_REQUIRED = "Este campo es obligatorio";

    /**
     * Mensaje genérico para valor inválido.
     */
    public static final String INVALID_VALUE = "El valor proporcionado no es válido";

    /**
     * Mensaje genérico para formato incorrecto.
     */
    public static final String INVALID_FORMAT = "El formato del campo es incorrecto";

    // ==================== CONSTRUCTOR PRIVADO ====================

    /**
     * Constructor privado para prevenir instanciación.
     * Esta es una clase de utilidad que solo contiene constantes estáticas.
     * 
     * @throws UnsupportedOperationException si se intenta instanciar
     */
    private ValidationConstants() {
        throw new UnsupportedOperationException(
                "ValidationConstants es una clase de utilidad y no debe ser instanciada"
        );
    }
}
