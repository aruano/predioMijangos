package com.predio.mijangos.security.service;

import com.predio.mijangos.modules.usuarios.domain.RefreshToken;
import com.predio.mijangos.modules.usuarios.domain.Usuario;

import java.util.Optional;

/**
 * Servicio de gestión de Refresh Tokens.
 * 
 * <p>Maneja el ciclo de vida completo de los refresh tokens, incluyendo
 * creación, validación, renovación y revocación.
 * 
 * <p><b>Responsabilidades:</b>
 * <ul>
 *   <li>Crear refresh tokens asociados a usuarios y dispositivos</li>
 *   <li>Validar tokens (expiración, revocación)</li>
 *   <li>Revocar tokens individuales o todos los tokens de un usuario</li>
 *   <li>Limpiar tokens expirados de la base de datos</li>
 *   <li>Gestionar información de dispositivo y ubicación</li>
 * </ul>
 * 
 * <p><b>Características de los refresh tokens:</b>
 * <ul>
 *   <li><b>Formato:</b> UUID aleatorio único</li>
 *   <li><b>Duración:</b> 7 días (configurable)</li>
 *   <li><b>Uso:</b> Renovar access tokens sin re-autenticación</li>
 *   <li><b>Seguridad:</b> Almacenados en base de datos, no en JWT</li>
 *   <li><b>Revocables:</b> Pueden invalidarse antes de expirar</li>
 * </ul>
 * 
 * <p><b>Casos de uso:</b>
 * <ul>
 *   <li>Usuario hace login → Se crea refresh token</li>
 *   <li>Access token expira → Se usa refresh token para renovarlo</li>
 *   <li>Usuario hace logout → Se revoca refresh token</li>
 *   <li>Cambio de contraseña → Se revocan todos los tokens del usuario</li>
 *   <li>Tarea programada → Limpia tokens expirados de la BD</li>
 * </ul>
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 * 
 * @see RefreshToken
 * @see com.predio.mijangos.security.service.AuthenticationService
 */
public interface RefreshTokenService {
    
    /**
     * Crea un nuevo refresh token para un usuario.
     * 
     * <p>Genera un token UUID único y lo asocia al usuario proporcionado.
     * Registra información del dispositivo y ubicación para auditoría
     * y gestión de sesiones.
     * 
     * <p><b>Proceso:</b>
     * <ol>
     *   <li>Genera UUID único</li>
     *   <li>Calcula fecha de expiración (7 días desde ahora)</li>
     *   <li>Asocia al usuario, dispositivo e IP</li>
     *   <li>Persiste en la base de datos</li>
     *   <li>Retorna el token creado</li>
     * </ol>
     * 
     * <p><b>Información registrada:</b>
     * <ul>
     *   <li><b>Usuario:</b> Relación con la entidad Usuario</li>
     *   <li><b>Device Info:</b> User-Agent del navegador/app</li>
     *   <li><b>IP Address:</b> Dirección IP del cliente</li>
     *   <li><b>Created At:</b> Timestamp de creación</li>
     *   <li><b>Expiry Date:</b> Timestamp de expiración (7 días)</li>
     * </ul>
     * 
     * <p><b>Uso típico:</b>
     * <pre>
     * RefreshToken token = refreshTokenService.createRefreshToken(
     *     usuario,
     *     request.getHeader("User-Agent"),
     *     request.getRemoteAddr()
     * );
     * </pre>
     * 
     * @param usuario Usuario para el que se crea el token (no puede ser null)
     * @param deviceInfo Información del dispositivo (User-Agent)
     * @param ipAddress Dirección IP del cliente
     * @return RefreshToken creado y persistido en la base de datos
     * 
     * @throws IllegalArgumentException si el usuario es null
     */
    RefreshToken createRefreshToken(Usuario usuario, String deviceInfo, String ipAddress);
    
    /**
     * Busca un refresh token por su valor de token.
     * 
     * <p>Realiza una búsqueda en la base de datos del token con el
     * valor proporcionado. El token se busca independientemente de
     * su estado (activo, revocado o expirado).
     * 
     * <p><b>Nota:</b> Este método solo busca el token, no valida
     * su estado. Para validar, usar {@link #isTokenValid(RefreshToken)}.
     * 
     * @param token Valor del token (UUID) a buscar
     * @return Optional con el RefreshToken si existe, Optional.empty() si no
     * 
     * @see #isTokenValid(RefreshToken)
     */
    Optional<RefreshToken> findByToken(String token);
    
    /**
     * Verifica si un refresh token es válido para uso.
     * 
     * <p>Un token es válido si cumple todas estas condiciones:
     * <ul>
     *   <li>No está revocado (revoked = false)</li>
     *   <li>No ha expirado (expiryDate &gt; now)</li>
     *   <li>El usuario asociado está activo</li>
     * </ul>
     * 
     * <p><b>Estados posibles:</b>
     * <table border="1">
     *   <tr>
     *     <th>Condición</th>
     *     <th>Resultado</th>
     *   </tr>
     *   <tr>
     *     <td>Token revocado</td>
     *     <td>false - Token inválido</td>
     *   </tr>
     *   <tr>
     *     <td>Token expirado</td>
     *     <td>false - Token inválido</td>
     *   </tr>
     *   <tr>
     *     <td>Usuario inactivo</td>
     *     <td>false - Token inválido</td>
     *   </tr>
     *   <tr>
     *     <td>Todo OK</td>
     *     <td>true - Token válido</td>
     *   </tr>
     * </table>
     * 
     * <p><b>Uso típico:</b>
     * <pre>
     * Optional&lt;RefreshToken&gt; tokenOpt = findByToken(tokenValue);
     * if (tokenOpt.isPresent() &amp;&amp; isTokenValid(tokenOpt.get())) {
     *     // Token válido, proceder con renovación
     * }
     * </pre>
     * 
     * @param token RefreshToken a validar
     * @return true si el token es válido para renovar access token, false en caso contrario
     * 
     * @throws IllegalArgumentException si token es null
     */
    boolean isTokenValid(RefreshToken token);
    
    /**
     * Revoca un refresh token específico.
     * 
     * <p>Marca el token como revocado, impidiendo su uso futuro
     * para renovar access tokens. Esta es una operación irreversible.
     * 
     * <p><b>Proceso:</b>
     * <ol>
     *   <li>Establece revoked = true</li>
     *   <li>Establece revokedAt = timestamp actual</li>
     *   <li>Persiste los cambios en la base de datos</li>
     * </ol>
     * 
     * <p><b>Cuándo usar:</b>
     * <ul>
     *   <li>Usuario hace logout normal</li>
     *   <li>Token comprometido detectado</li>
     *   <li>Solicitud de cierre de sesión en dispositivo específico</li>
     * </ul>
     * 
     * <p><b>Nota importante:</b><br>
     * Revocar el refresh token NO invalida el access token JWT
     * asociado. El access token seguirá siendo válido hasta su
     * expiración natural (8 horas).
     * 
     * @param token RefreshToken a revocar
     * 
     * @throws IllegalArgumentException si token es null
     */
    void revokeToken(RefreshToken token);
    
    /**
     * Revoca todos los refresh tokens de un usuario.
     * 
     * <p>Cierra todas las sesiones activas del usuario en todos
     * los dispositivos, forzando un nuevo login en cada uno.
     * 
     * <p><b>Proceso:</b>
     * <ol>
     *   <li>Busca todos los tokens del usuario</li>
     *   <li>Marca cada token como revocado</li>
     *   <li>Establece revokedAt en cada uno</li>
     *   <li>Persiste los cambios</li>
     * </ol>
     * 
     * <p><b>Cuándo usar:</b>
     * <ul>
     *   <li>Usuario cambia su contraseña</li>
     *   <li>Usuario solicita cerrar todas las sesiones</li>
     *   <li>Sospecha de cuenta comprometida</li>
     *   <li>Usuario deshabilitado por administrador</li>
     * </ul>
     * 
     * <p><b>Uso típico:</b>
     * <pre>
     * // Después de cambiar contraseña
     * usuarioService.cambiarPassword(userId, newPassword);
     * refreshTokenService.revokeAllUserTokens(userId);
     * </pre>
     * 
     * @param idUsuario ID del usuario cuyos tokens se revocarán
     * 
     * @throws IllegalArgumentException si idUsuario es null
     */
    void revokeAllUserTokens(Integer idUsuario);
    
    /**
     * Elimina físicamente tokens expirados de la base de datos.
     * 
     * <p>Limpia tokens que ya expiraron para mantener la base de datos
     * optimizada. Este método debe ejecutarse periódicamente mediante
     * una tarea programada.
     * 
     * <p><b>Criterio de eliminación:</b><br>
     * Se eliminan tokens donde {@code expiryDate < now()}
     * 
     * <p><b>Ventajas de la limpieza:</b>
     * <ul>
     *   <li>Reduce tamaño de la tabla TBL_Refresh_Token</li>
     *   <li>Mejora rendimiento de consultas</li>
     *   <li>Libera espacio en disco</li>
     *   <li>Cumple con políticas de retención de datos</li>
     * </ul>
     * 
     * <p><b>Configuración recomendada:</b>
     * <pre>
     * &#64;Scheduled(cron = "0 0 2 * * ?") // Todos los días a las 2 AM
     * public void cleanupExpiredTokens() {
     *     refreshTokenService.deleteExpiredTokens();
     * }
     * </pre>
     * 
     * <p><b>Nota:</b> Esta operación es segura ya que solo elimina
     * tokens que de todas formas ya no son utilizables.
     * 
     * @return Número de tokens eliminados (útil para logging)
     */
    void deleteExpiredTokens();
}
