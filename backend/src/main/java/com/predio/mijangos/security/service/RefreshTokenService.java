package com.predio.mijangos.security.service;

import com.predio.mijangos.core.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servicio para gestión de Refresh Tokens.
 * 
 * Los refresh tokens permiten obtener nuevos access tokens sin necesidad
 * de volver a autenticarse con credenciales.
 * 
 * Características:
 * - Mayor duración que los access tokens (7 días vs 8 horas)
 * - Un usuario puede tener múltiples refresh tokens activos (múltiples dispositivos)
 * - Se invalidan al cerrar sesión o cambiar contraseña
 * - Se almacenan de forma segura en la base de datos
 * 
 * NOTA: Esta implementación usa un Map en memoria como almacenamiento temporal.
 * En producción, debe reemplazarse por una entidad RefreshToken en la BD.
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenDurationMs;

    // TODO: Reemplazar con RefreshTokenRepository cuando esté disponible
    // private final RefreshTokenRepository refreshTokenRepository;
    
    // Almacenamiento temporal en memoria (solo para desarrollo)
    // ⚠️ ELIMINAR en producción - usar base de datos
    private final Map<String, RefreshTokenData> tokenStore = new ConcurrentHashMap<>();

    /**
     * Crea un nuevo refresh token para un usuario.
     * 
     * @param username Código de empleado del usuario
     * @return Token generado (UUID)
     */
    @Transactional
    public String createRefreshToken(String username) {
        log.debug("Creando refresh token para usuario: {}", username);
        
        // Generar token único
        String token = UUID.randomUUID().toString();
        
        // Calcular fecha de expiración
        LocalDateTime expiryDate = LocalDateTime.now()
                .plusSeconds(refreshTokenDurationMs / 1000);
        
        // TODO: Guardar en base de datos
        /*
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setUsername(username);
        refreshToken.setExpiryDate(expiryDate);
        refreshToken.setCreatedAt(LocalDateTime.now());
        
        refreshTokenRepository.save(refreshToken);
        */
        
        // Implementación temporal en memoria
        tokenStore.put(token, new RefreshTokenData(username, expiryDate));
        
        log.info("Refresh token creado para usuario: {}", username);
        return token;
    }

    /**
     * Valida un refresh token.
     * 
     * @param token Refresh token a validar
     * @return true si el token es válido, false en caso contrario
     */
    @Transactional(readOnly = true)
    public boolean validateRefreshToken(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }
        
        // TODO: Buscar en base de datos
        /*
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByToken(token);
        
        if (refreshTokenOpt.isEmpty()) {
            return false;
        }
        
        RefreshToken refreshToken = refreshTokenOpt.get();
        
        // Verificar si ha expirado
        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            // Eliminar token expirado
            refreshTokenRepository.delete(refreshToken);
            return false;
        }
        
        return true;
        */
        
        // Implementación temporal en memoria
        RefreshTokenData tokenData = tokenStore.get(token);
        
        if (tokenData == null) {
            return false;
        }
        
        if (tokenData.expiryDate.isBefore(LocalDateTime.now())) {
            tokenStore.remove(token);
            return false;
        }
        
        return true;
    }

    /**
     * Obtiene el username asociado a un refresh token.
     * 
     * @param token Refresh token
     * @return Username del usuario
     * @throws BusinessException Si el token no existe o ha expirado
     */
    @Transactional(readOnly = true)
    public String getUsernameFromRefreshToken(String token) {
        // TODO: Buscar en base de datos
        /*
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException("INVALID_REFRESH_TOKEN", 
                        "Refresh token no encontrado"));
        
        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new BusinessException("EXPIRED_REFRESH_TOKEN", 
                    "Refresh token ha expirado");
        }
        
        return refreshToken.getUsername();
        */
        
        // Implementación temporal en memoria
        RefreshTokenData tokenData = tokenStore.get(token);
        
        if (tokenData == null) {
            throw new BusinessException("INVALID_REFRESH_TOKEN", 
                    "Refresh token no encontrado");
        }
        
        if (tokenData.expiryDate.isBefore(LocalDateTime.now())) {
            tokenStore.remove(token);
            throw new BusinessException("EXPIRED_REFRESH_TOKEN", 
                    "Refresh token ha expirado");
        }
        
        return tokenData.username;
    }

    /**
     * Elimina un refresh token específico.
     * Se usa al cerrar sesión.
     * 
     * @param token Refresh token a eliminar
     */
    @Transactional
    public void deleteRefreshToken(String token) {
        log.debug("Eliminando refresh token");
        
        // TODO: Eliminar de base de datos
        /*
        refreshTokenRepository.findByToken(token)
                .ifPresent(refreshTokenRepository::delete);
        */
        
        // Implementación temporal en memoria
        tokenStore.remove(token);
        
        log.debug("Refresh token eliminado");
    }

    /**
     * Elimina todos los refresh tokens de un usuario.
     * Se usa al cambiar contraseña o por seguridad.
     * 
     * @param username Código de empleado del usuario
     */
    @Transactional
    public void deleteAllUserRefreshTokens(String username) {
        log.info("Eliminando todos los refresh tokens del usuario: {}", username);
        
        // TODO: Eliminar de base de datos
        /*
        List<RefreshToken> userTokens = refreshTokenRepository.findAllByUsername(username);
        refreshTokenRepository.deleteAll(userTokens);
        */
        
        // Implementación temporal en memoria
        tokenStore.entrySet().removeIf(entry -> 
                entry.getValue().username.equals(username));
        
        log.info("Refresh tokens eliminados para usuario: {}", username);
    }

    /**
     * Elimina todos los refresh tokens expirados del sistema.
     * Debe ejecutarse periódicamente (ej: tarea programada diaria).
     */
    @Transactional
    public void deleteExpiredTokens() {
        log.info("Limpiando refresh tokens expirados");
        
        // TODO: Eliminar de base de datos
        /*
        LocalDateTime now = LocalDateTime.now();
        List<RefreshToken> expiredTokens = refreshTokenRepository
                .findAllByExpiryDateBefore(now);
        refreshTokenRepository.deleteAll(expiredTokens);
        */
        
        // Implementación temporal en memoria
        LocalDateTime now = LocalDateTime.now();
        tokenStore.entrySet().removeIf(entry -> 
                entry.getValue().expiryDate.isBefore(now));
        
        log.info("Refresh tokens expirados eliminados");
    }

    /**
     * Cuenta los refresh tokens activos de un usuario.
     * Útil para limitar el número de dispositivos simultáneos.
     * 
     * @param username Código de empleado del usuario
     * @return Número de tokens activos
     */
    @Transactional(readOnly = true)
    public long countUserActiveTokens(String username) {
        // TODO: Contar en base de datos
        /*
        LocalDateTime now = LocalDateTime.now();
        return refreshTokenRepository.countByUsernameAndExpiryDateAfter(username, now);
        */
        
        // Implementación temporal en memoria
        LocalDateTime now = LocalDateTime.now();
        return tokenStore.values().stream()
                .filter(data -> data.username.equals(username))
                .filter(data -> data.expiryDate.isAfter(now))
                .count();
    }

    /**
     * Clase interna para almacenamiento temporal de datos del token.
     * ⚠️ ELIMINAR cuando se implemente RefreshToken entity.
     */
    private static class RefreshTokenData {
        final String username;
        final LocalDateTime expiryDate;

        RefreshTokenData(String username, LocalDateTime expiryDate) {
            this.username = username;
            this.expiryDate = expiryDate;
        }
    }
}

/**
 * IMPORTANTE: Entidad RefreshToken para implementar en el módulo de seguridad/usuarios
 * 
 * @Entity
 * @Table(name = "TBL_Refresh_Token")
 * public class RefreshToken {
 *     
 *     @Id
 *     @GeneratedValue(strategy = GenerationType.IDENTITY)
 *     private Integer id;
 *     
 *     @Column(name = "token", nullable = false, unique = true, length = 255)
 *     private String token;
 *     
 *     @Column(name = "username", nullable = false, length = 50)
 *     private String username;
 *     
 *     @Column(name = "expiry_date", nullable = false)
 *     private LocalDateTime expiryDate;
 *     
 *     @Column(name = "created_at", nullable = false)
 *     private LocalDateTime createdAt;
 *     
 *     // Getters y Setters
 * }
 * 
 * // Repository
 * public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
 *     Optional<RefreshToken> findByToken(String token);
 *     List<RefreshToken> findAllByUsername(String username);
 *     List<RefreshToken> findAllByExpiryDateBefore(LocalDateTime date);
 *     Long countByUsernameAndExpiryDateAfter(String username, LocalDateTime date);
 * }
 */