package com.predio.mijangos.security.service.impl;

import com.predio.mijangos.modules.usuarios.domain.RefreshToken;
import com.predio.mijangos.modules.usuarios.domain.Usuario;
import com.predio.mijangos.modules.usuarios.repository.RefreshTokenRepository;
import com.predio.mijangos.security.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Implementación del servicio de gestión de Refresh Tokens.
 * 
 * <p>Proporciona funcionalidad completa para el ciclo de vida de los
 * refresh tokens, incluyendo creación, validación, revocación y limpieza.
 * 
 * <p><b>Características de implementación:</b>
 * <ul>
 *   <li>Generación de tokens UUID únicos</li>
 *   <li>Registro de información de dispositivo e IP para auditoría</li>
 *   <li>Validación de expiración y estado de revocación</li>
 *   <li>Limpieza automática programada de tokens expirados</li>
 *   <li>Soporte para múltiples sesiones por usuario</li>
 * </ul>
 * 
 * <p><b>Configuración:</b><br>
 * La duración de los tokens se configura mediante:
 * <pre>
 * jwt.refresh-expiration=604800000  # 7 días en milisegundos
 * </pre>
 * 
 * <p><b>Tareas programadas:</b><br>
 * Ejecuta limpieza automática de tokens expirados diariamente a las 2 AM.
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 * 
 * @see RefreshTokenService
 * @see RefreshToken
 * @see RefreshTokenRepository
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository repository;

    /**
     * Duración de validez del refresh token en milisegundos.
     * Por defecto: 604800000 ms (7 días).
     * Configurable mediante: jwt.refresh-expiration
     */
    @Value("${jwt.refresh-expiration:604800000}")
    private Long refreshTokenExpirationMs;

    /**
     * {@inheritDoc}
     * 
     * <p><b>Implementación:</b>
     * <ol>
     *   <li>Genera un UUID único como valor del token</li>
     *   <li>Calcula fecha de expiración (ahora + refreshTokenExpirationMs)</li>
     *   <li>Crea entidad RefreshToken con información del dispositivo</li>
     *   <li>Persiste en la base de datos</li>
     *   <li>Retorna el token creado</li>
     * </ol>
     * 
     * <p><b>Nota sobre múltiples sesiones:</b><br>
     * Por defecto, este método permite múltiples tokens activos por usuario
     * (múltiples sesiones simultáneas). Si se desea limitar a un token por
     * usuario, descomentar la línea que revoca tokens existentes.
     * 
     * <p><b>Información registrada:</b>
     * <ul>
     *   <li><b>Token:</b> UUID único generado aleatoriamente</li>
     *   <li><b>Usuario:</b> Relación con Usuario</li>
     *   <li><b>Expiry Date:</b> Timestamp actual + 7 días</li>
     *   <li><b>Device Info:</b> User-Agent del cliente</li>
     *   <li><b>IP Address:</b> IP del cliente</li>
     *   <li><b>Created At:</b> Timestamp de creación</li>
     *   <li><b>Revoked:</b> false (token activo)</li>
     * </ul>
     * 
     * @throws IllegalArgumentException si usuario es null
     */
    @Override
    @Transactional
    public RefreshToken createRefreshToken(Usuario usuario, String deviceInfo, String ipAddress) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }
        
        log.debug("Creando refresh token para usuario: {}", usuario.getUsuario());
        
        // OPCIONAL: Revocar tokens existentes para limitar a un token por usuario
        // Si se descomenta, el usuario solo podrá tener una sesión activa
        // revokeAllUserTokens(usuario.getId());
        
        // Calcular fecha de expiración
        LocalDateTime expiryDate = LocalDateTime.now()
            .plusSeconds(refreshTokenExpirationMs / 1000);
        
        log.debug("Token expirará en: {}", expiryDate);
        
        // Construir refresh token
        RefreshToken token = RefreshToken.builder()
            .token(UUID.randomUUID().toString())
            .usuario(usuario)
            .expiryDate(expiryDate)
            .deviceInfo(deviceInfo != null ? deviceInfo : "Unknown")
            .ipAddress(ipAddress != null ? ipAddress : "Unknown")
            .build();
        
        // Persistir token
        RefreshToken saved = repository.save(token);
        
        log.info("Refresh token creado exitosamente - Usuario: {}, Token ID: {}, Expira: {}", 
                usuario.getUsuario(), saved.getId(), expiryDate);
        
        return saved;
    }

    /**
     * {@inheritDoc}
     * 
     * <p><b>Implementación:</b><br>
     * Realiza una búsqueda por el valor exacto del token en la base de datos.
     * No valida el estado del token (use {@link #isTokenValid(RefreshToken)} para eso).
     */
    @Override
    public Optional<RefreshToken> findByToken(String token) {
        log.debug("Buscando refresh token por valor");
        
        Optional<RefreshToken> result = repository.findByToken(token);
        
        if (result.isPresent()) {
            log.debug("Refresh token encontrado - ID: {}", result.get().getId());
        } else {
            log.debug("Refresh token no encontrado");
        }
        
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * <p><b>Implementación:</b><br>
     * Valida tres condiciones en orden:
     * <ol>
     *   <li>Token no puede ser null</li>
     *   <li>Token no debe estar revocado</li>
     *   <li>Token no debe estar expirado (expiryDate &gt; now)</li>
     * </ol>
     * 
     * <p>Si alguna condición falla, retorna false inmediatamente.
     */
    @Override
    public boolean isTokenValid(RefreshToken token) {
        if (token == null) {
            log.warn("Validación de token: token es null");
            return false;
        }
        
        // Verificar revocación
        if (Boolean.TRUE.equals(token.getRevoked())) {
            log.debug("Validación de token: token ID {} está revocado", token.getId());
            return false;
        }
        
        // Verificar expiración
        if (token.isExpired()) {
            log.debug("Validación de token: token ID {} está expirado (expiró: {})", 
                     token.getId(), token.getExpiryDate());
            return false;
        }
        
        log.debug("Validación de token: token ID {} es válido", token.getId());
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * <p><b>Implementación:</b>
     * <ol>
     *   <li>Llama al método {@link RefreshToken#revoke()} del token</li>
     *   <li>Este método establece revoked=true y revokedAt=now</li>
     *   <li>Persiste los cambios en la base de datos</li>
     * </ol>
     * 
     * <p><b>Nota:</b> Es seguro llamar este método múltiples veces
     * sobre el mismo token sin efectos secundarios.
     * 
     * @throws IllegalArgumentException si token es null
     */
    @Override
    @Transactional
    public void revokeToken(RefreshToken token) {
        if (token == null) {
            throw new IllegalArgumentException("El token no puede ser null");
        }
        
        log.info("Revocando refresh token - ID: {}, Usuario: {}", 
                token.getId(), token.getUsuario().getUsuario());
        
        // Revocar el token (establece revoked=true y revokedAt=now)
        token.revoke();
        
        // Persistir cambios
        repository.save(token);
        
        log.debug("Refresh token revocado exitosamente - ID: {}", token.getId());
    }

    /**
     * {@inheritDoc}
     * 
     * <p><b>Implementación:</b><br>
     * Utiliza una consulta nativa optimizada que actualiza todos los
     * tokens del usuario en una sola operación SQL:
     * <pre>
     * UPDATE TBL_Refresh_Token
     * SET revoked = true, revoked_at = ?
     * WHERE id_usuario = ? AND revoked = false
     * </pre>
     * 
     * <p>Esta implementación es más eficiente que cargar y actualizar
     * cada token individualmente.
     * 
     * @throws IllegalArgumentException si idUsuario es null
     */
    @Override
    @Transactional
    public void revokeAllUserTokens(Integer idUsuario) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El ID de usuario no puede ser null");
        }
        
        log.info("Revocando todos los refresh tokens del usuario ID: {}", idUsuario);
        
        LocalDateTime revokedAt = LocalDateTime.now();
        
        // Ejecutar actualización masiva
        int tokensRevoked = repository.revokeAllByUsuarioId(idUsuario, revokedAt);
        
        log.info("Revocados {} refresh tokens del usuario ID: {}", tokensRevoked, idUsuario);
    }

    /**
     * {@inheritDoc}
     * 
     * <p><b>Implementación:</b>
     * <ol>
     *   <li>Ejecuta consulta que elimina tokens donde expiryDate &lt; now</li>
     *   <li>Utiliza operación DELETE nativa para mejor rendimiento</li>
     *   <li>Registra el número de tokens eliminados</li>
     * </ol>
     * 
     * <p><b>Programación:</b><br>
     * Este método se ejecuta automáticamente todos los días a las 2 AM
     * mediante la anotación @Scheduled.
     * 
     * <p><b>Configuración de la tarea:</b>
     * <ul>
     *   <li><b>Cron:</b> "0 0 2 * * ?" (2 AM diario)</li>
     *   <li><b>Zona horaria:</b> Sistema por defecto</li>
     *   <li><b>Requisito:</b> @EnableScheduling en clase de configuración</li>
     * </ul>
     * 
     * <p><b>Monitoreo recomendado:</b><br>
     * Los logs INFO indican inicio y fin de la limpieza con el
     * número de registros eliminados. Monitorear estos logs ayuda
     * a detectar problemas de rendimiento o acumulación excesiva.
     */
    @Override
    @Transactional
    @Scheduled(cron = "0 0 2 * * ?") // Ejecutar a las 2 AM todos los días
    public void deleteExpiredTokens() {
        log.info("Iniciando limpieza de refresh tokens expirados");
        
        LocalDateTime now = LocalDateTime.now();
        
        // Eliminar tokens expirados
        int deletedCount = repository.deleteExpiredTokens(now);
        
        log.info("Limpieza completada - {} refresh tokens expirados eliminados", deletedCount);
    }
}
