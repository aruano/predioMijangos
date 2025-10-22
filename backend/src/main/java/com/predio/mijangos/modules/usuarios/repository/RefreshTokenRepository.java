package com.predio.mijangos.modules.usuarios.repository;

import com.predio.mijangos.modules.usuarios.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repositorio para la gestión de Refresh Tokens.
 * 
 * <p>Proporciona operaciones CRUD y consultas personalizadas para
 * la gestión de tokens de refresco utilizados en el sistema de
 * autenticación JWT.
 * 
 * <p><b>Operaciones principales:</b>
 * <ul>
 *   <li>Búsqueda de tokens por valor</li>
 *   <li>Búsqueda de tokens válidos por usuario</li>
 *   <li>Revocación masiva de tokens de un usuario</li>
 *   <li>Eliminación de tokens expirados</li>
 *   <li>Eliminación de todos los tokens de un usuario</li>
 * </ul>
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 * 
 * @see RefreshToken
 * @see com.predio.mijangos.security.service.RefreshTokenService
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    
    /**
     * Busca un refresh token por su valor único.
     * 
     * <p>Este método busca el token independientemente de su estado
     * (revocado o expirado). Para verificar validez, usar
     * {@link #findValidTokenByUsuarioId(Integer, LocalDateTime)}.
     * 
     * @param token Valor del token (UUID) a buscar
     * @return Optional con el RefreshToken si existe, Optional.empty() si no
     */
    Optional<RefreshToken> findByToken(String token);
    
    /**
     * Busca un token válido (no revocado y no expirado) para un usuario.
     * 
     * <p>Un token es considerado válido si:
     * <ul>
     *   <li>revoked = false</li>
     *   <li>expiryDate &gt; now</li>
     * </ul>
     * 
     * <p><b>Uso típico:</b>
     * <pre>
     * Optional&lt;RefreshToken&gt; validToken = repository.findValidTokenByUsuarioId(
     *     userId, 
     *     LocalDateTime.now()
     * );
     * </pre>
     * 
     * @param idUsuario ID del usuario propietario del token
     * @param now Timestamp actual para comparar con expiryDate
     * @return Optional con el token válido si existe, Optional.empty() si no
     */
    @Query("SELECT rt FROM RefreshToken rt WHERE rt.usuario.id = :idUsuario " +
           "AND rt.revoked = false AND rt.expiryDate > :now")
    Optional<RefreshToken> findValidTokenByUsuarioId(
        @Param("idUsuario") Integer idUsuario,
        @Param("now") LocalDateTime now
    );
    
    /**
     * Revoca todos los refresh tokens activos de un usuario.
     * 
     * <p>Ejecuta una actualización masiva que establece:
     * <ul>
     *   <li>revoked = true</li>
     *   <li>revokedAt = now</li>
     * </ul>
     * 
     * <p>Solo afecta tokens que actualmente tienen revoked = false.
     * 
     * <p><b>Uso típico:</b><br>
     * Se ejecuta cuando:
     * <ul>
     *   <li>Usuario cambia su contraseña</li>
     *   <li>Usuario solicita cerrar todas las sesiones</li>
     *   <li>Administrador desactiva al usuario</li>
     *   <li>Detección de actividad sospechosa</li>
     * </ul>
     * 
     * <p><b>Nota:</b> Este método requiere @Transactional en el servicio llamador.
     * 
     * @param idUsuario ID del usuario cuyos tokens se revocarán
     * @param now Timestamp actual para establecer en revokedAt
     * @return Número de tokens revocados (filas afectadas)
     */
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.revoked = true, rt.revokedAt = :now " +
           "WHERE rt.usuario.id = :idUsuario AND rt.revoked = false")
    int revokeAllByUsuarioId(
        @Param("idUsuario") Integer idUsuario,
        @Param("now") LocalDateTime now
    );
    
    /**
     * Elimina físicamente tokens expirados de la base de datos.
     * 
     * <p>Ejecuta una operación DELETE que remueve permanentemente
     * todos los tokens cuya fecha de expiración es anterior al
     * timestamp proporcionado.
     * 
     * <p><b>Criterio de eliminación:</b>
     * <pre>
     * expiryDate &lt; now
     * </pre>
     * 
     * <p><b>Uso recomendado:</b><br>
     * Ejecutar periódicamente mediante tarea programada para:
     * <ul>
     *   <li>Mantener la tabla limpia</li>
     *   <li>Mejorar rendimiento de consultas</li>
     *   <li>Liberar espacio en disco</li>
     *   <li>Cumplir políticas de retención de datos</li>
     * </ul>
     * 
     * <p><b>Ejemplo de tarea programada:</b>
     * <pre>
     * &#64;Scheduled(cron = "0 0 2 * * ?") // 2 AM diario
     * public void cleanupExpiredTokens() {
     *     int deleted = repository.deleteExpiredTokens(LocalDateTime.now());
     *     log.info("Eliminados {} tokens expirados", deleted);
     * }
     * </pre>
     * 
     * <p><b>Nota:</b> Este método requiere @Transactional en el servicio llamador.
     * 
     * @param now Timestamp actual para comparar con expiryDate
     * @return Número de tokens eliminados (filas afectadas)
     */
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiryDate < :now")
    int deleteExpiredTokens(@Param("now") LocalDateTime now);
    
    /**
     * Elimina todos los refresh tokens de un usuario.
     * 
     * <p>Operación de eliminación física (no soft delete) que remueve
     * permanentemente todos los tokens asociados a un usuario,
     * independientemente de su estado.
     * 
     * <p><b>Uso típico:</b><br>
     * Se ejecuta cuando:
     * <ul>
     *   <li>Usuario es eliminado del sistema (cascade)</li>
     *   <li>Limpieza completa de sesiones de un usuario</li>
     *   <li>Reset total de credenciales de un usuario</li>
     * </ul>
     * 
     * <p><b>Advertencia:</b> Esta operación es irreversible.
     * El usuario deberá volver a iniciar sesión para obtener nuevos tokens.
     * 
     * <p><b>Nota:</b> Este método requiere @Transactional en el servicio llamador.
     * 
     * @param idUsuario ID del usuario cuyos tokens se eliminarán
     * @return Número de tokens eliminados (filas afectadas)
     */
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.usuario.id = :idUsuario")
    int deleteByUsuarioId(@Param("idUsuario") Integer idUsuario);
}
