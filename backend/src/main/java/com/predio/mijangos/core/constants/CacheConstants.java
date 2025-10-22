package com.predio.mijangos.core.constants;

/**
 * Constantes para configuración de caché en la aplicación.
 * 
 * <p>Define nombres de caché y tiempos de expiración para datos
 * que se cachean en el sistema. El caché mejora el rendimiento
 * reduciendo consultas repetitivas a la base de datos.
 * 
 * <p><b>Uso con Spring Cache:</b>
 * <pre>
 * {@literal @}Cacheable(
 *     value = CacheConstants.DEPARTAMENTOS_CACHE,
 *     key = "#id"
 * )
 * public DepartamentoDTO findById(Integer id) {
 *     // ...
 * }
 * </pre>
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
public final class CacheConstants {

    // ==================== NOMBRES DE CACHÉ ====================

    /**
     * Caché para departamentos (datos geográficos).
     * 
     * <p>Los departamentos son datos de referencia que raramente cambian,
     * por lo que son buenos candidatos para caché de larga duración.
     */
    public static final String DEPARTAMENTOS_CACHE = "departamentos";

    /**
     * Caché para municipios (datos geográficos).
     * 
     * <p>Similar a departamentos, los municipios son datos estables
     * que se consultan frecuentemente.
     */
    public static final String MUNICIPIOS_CACHE = "municipios";

    /**
     * Caché para roles del sistema.
     * 
     * <p>Los roles son prácticamente estáticos después de la configuración
     * inicial del sistema, ideales para caché de larga duración.
     */
    public static final String ROLES_CACHE = "roles";

    /**
     * Caché para módulos del sistema.
     * 
     * <p>Los módulos representan las secciones de la aplicación y
     * cambian muy raramente.
     */
    public static final String MODULOS_CACHE = "modulos";

    /**
     * Caché para páginas/permisos del sistema.
     * 
     * <p>Las páginas y sus permisos asociados son configuración estática
     * que se consulta frecuentemente en validaciones de autorización.
     */
    public static final String PAGINAS_CACHE = "paginas";

    /**
     * Caché para configuraciones del sistema.
     * 
     * <p>Configuraciones generales de la aplicación que se consultan
     * frecuentemente pero cambian muy poco.
     */
    public static final String CONFIGURACIONES_CACHE = "configuraciones";

    /**
     * Caché para categorías de productos.
     * 
     * <p>Las categorías de productos son relativamente estables
     * y se consultan en cada listado de productos.
     */
    public static final String CATEGORIAS_CACHE = "categorias";

    /**
     * Caché para datos de usuarios (información básica).
     * 
     * <p>Información básica de usuario que se consulta frecuentemente
     * pero puede cambiar ocasionalmente.
     * 
     * <p><b>NOTA:</b> No cachear información sensible como contraseñas.
     */
    public static final String USUARIOS_CACHE = "usuarios";

    // ==================== TIEMPOS DE EXPIRACIÓN (en segundos) ====================

    /**
     * Tiempo de expiración para datos geográficos (1 hora).
     * 
     * <p>Departamentos y municipios pueden cachearse por largo tiempo
     * ya que son datos que prácticamente no cambian.
     * 
     * <p>Valor: 3600 segundos = 1 hora
     */
    public static final long GEO_CACHE_TTL = 3600L;

    /**
     * Tiempo de expiración para roles y permisos (30 minutos).
     * 
     * <p>Roles y permisos son configuración del sistema que cambia muy poco,
     * pero es importante que los cambios se reflejen en tiempo razonable.
     * 
     * <p>Valor: 1800 segundos = 30 minutos
     */
    public static final long ROLES_CACHE_TTL = 1800L;

    /**
     * Tiempo de expiración para módulos y páginas (30 minutos).
     * 
     * <p>Similar a roles, los módulos y páginas cambian poco pero deben
     * reflejarse en tiempo razonable.
     * 
     * <p>Valor: 1800 segundos = 30 minutos
     */
    public static final long MODULOS_CACHE_TTL = 1800L;

    /**
     * Tiempo de expiración para configuraciones (15 minutos).
     * 
     * <p>Configuraciones pueden cambiar con más frecuencia que otros
     * datos de referencia.
     * 
     * <p>Valor: 900 segundos = 15 minutos
     */
    public static final long CONFIGURACIONES_CACHE_TTL = 900L;

    /**
     * Tiempo de expiración para categorías (15 minutos).
     * 
     * <p>Las categorías pueden cambiar ocasionalmente cuando se agregan
     * nuevas líneas de productos.
     * 
     * <p>Valor: 900 segundos = 15 minutos
     */
    public static final long CATEGORIAS_CACHE_TTL = 900L;

    /**
     * Tiempo de expiración para datos de usuarios (5 minutos).
     * 
     * <p>Información de usuario puede cambiar con más frecuencia
     * (nombre, email, rol, etc.) por lo que debe tener un TTL corto.
     * 
     * <p>Valor: 300 segundos = 5 minutos
     */
    public static final long USUARIOS_CACHE_TTL = 300L;

    // ==================== CONFIGURACIÓN DE CACHÉ ====================

    /**
     * Tamaño máximo de caché (número de entradas).
     * 
     * <p>Límite de entradas por caché para evitar consumo excesivo de memoria.
     * Cuando se alcanza el límite, se usa política LRU (Least Recently Used).
     * 
     * <p>Valor: 1000 entradas
     */
    public static final int MAX_CACHE_SIZE = 1000;

    /**
     * Indica si el caché debe ser cargado al inicio de la aplicación.
     * 
     * <p>Para datos de referencia críticos, puede ser útil pre-cargar
     * el caché al iniciar la aplicación.
     * 
     * <p>Valor: true
     */
    public static final boolean PRELOAD_CACHE = true;

    // ==================== POLÍTICAS DE INVALIDACIÓN ====================

    /**
     * Indica si se debe invalidar caché automáticamente al actualizar.
     * 
     * <p>Cuando un recurso es actualizado, su entrada en caché debe
     * ser invalidada para evitar datos obsoletos.
     * 
     * <p>Valor: true
     */
    public static final boolean AUTO_EVICT_ON_UPDATE = true;

    /**
     * Indica si se debe invalidar caché automáticamente al eliminar.
     * 
     * <p>Cuando un recurso es eliminado (soft delete), su entrada debe
     * ser removida del caché.
     * 
     * <p>Valor: true
     */
    public static final boolean AUTO_EVICT_ON_DELETE = true;

    // ==================== MENSAJES DE LOG ====================

    /**
     * Mensaje de log cuando se cachea un recurso.
     */
    public static final String CACHE_HIT_MESSAGE = "Cache hit para: {}";

    /**
     * Mensaje de log cuando no se encuentra en caché.
     */
    public static final String CACHE_MISS_MESSAGE = "Cache miss para: {}";

    /**
     * Mensaje de log cuando se invalida caché.
     */
    public static final String CACHE_EVICT_MESSAGE = "Cache evicted para: {}";

    // ==================== CONSTRUCTOR PRIVADO ====================

    /**
     * Constructor privado para prevenir instanciación.
     * Esta es una clase de utilidad que solo contiene constantes estáticas.
     * 
     * @throws UnsupportedOperationException si se intenta instanciar
     */
    private CacheConstants() {
        throw new UnsupportedOperationException(
                "CacheConstants es una clase de utilidad y no debe ser instanciada"
        );
    }
}
