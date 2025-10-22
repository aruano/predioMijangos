package com.predio.mijangos.security.service.impl;

import com.predio.mijangos.modules.usuarios.domain.RefreshToken;
import com.predio.mijangos.modules.usuarios.domain.Rol;
import com.predio.mijangos.modules.usuarios.domain.Usuario;
import com.predio.mijangos.modules.usuarios.repository.UsuarioRepository;
import com.predio.mijangos.security.dto.LoginRequestDTO;
import com.predio.mijangos.security.dto.LoginResponseDTO;
import com.predio.mijangos.security.dto.LoginResponseDTO.UsuarioInfoDTO;
import com.predio.mijangos.security.jwt.JwtUtil;
import com.predio.mijangos.security.model.CustomUserDetails;
import com.predio.mijangos.security.service.AuthenticationService;
import com.predio.mijangos.security.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de autenticación.
 * 
 * <p>Gestiona el proceso completo de autenticación de usuarios, incluyendo
 * la generación y renovación de tokens JWT y refresh tokens.
 * 
 * <p><b>Características principales:</b>
 * <ul>
 *   <li>Autenticación mediante Spring Security</li>
 *   <li>Generación de JWT tokens para autorización</li>
 *   <li>Gestión de refresh tokens para renovación de sesión</li>
 *   <li>Tracking de información de dispositivo y ubicación</li>
 *   <li>Revocación de tokens al cerrar sesión</li>
 * </ul>
 * 
 * <p><b>Seguridad:</b>
 * <ul>
 *   <li>Valida que el usuario esté activo antes de autenticar</li>
 *   <li>Registra información de dispositivo y ubicación en cada login</li>
 *   <li>Verifica expiración y estado de refresh tokens</li>
 *   <li>Maneja excepciones de autenticación apropiadamente</li>
 * </ul>
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;
    private final RefreshTokenService refreshTokenService;
    private final HttpServletRequest request;

    /**
     * {@inheritDoc}
     * 
     * <p><b>Implementación:</b>
     * <ol>
     *   <li>Autentica credenciales usando AuthenticationManager de Spring Security</li>
     *   <li>Recupera el usuario completo de la base de datos con sus roles</li>
     *   <li>Valida que el usuario esté activo</li>
     *   <li>Genera un access token JWT con información del usuario</li>
     *   <li>Crea un refresh token asociado al usuario y dispositivo</li>
     *   <li>Registra información del dispositivo y ubicación (IP)</li>
     *   <li>Retorna ambos tokens junto con la información del usuario</li>
     * </ol>
     * 
     * @throws BadCredentialsException si las credenciales son incorrectas
     * @throws DisabledException si el usuario está inactivo
     * @throws UsernameNotFoundException si el usuario no existe
     */
    @Override
    @Transactional
    public LoginResponseDTO login(LoginRequestDTO loginRequest) {
        log.info("Intento de login para usuario: {}", loginRequest.username());
        
        try {
            // 1. Autenticar credenciales
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.username(),
                    loginRequest.password()
                )
            );
            
            // 2. Establecer autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 3. Obtener usuario completo con roles
            Usuario usuario = usuarioRepository.findByUsuarioWithRoles(loginRequest.username())
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado después de autenticación exitosa: {}", 
                             loginRequest.username());
                    return new UsernameNotFoundException(
                        "Usuario no encontrado: " + loginRequest.username()
                    );
                });
            
            // 4. Validar que el usuario esté activo
            if (!usuario.getActivo()) {
                log.warn("Intento de login con usuario inactivo: {}", loginRequest.username());
                throw new DisabledException("Usuario inactivo: " + loginRequest.username());
            }
            
            // 5. Obtener UserDetails del contexto de autenticación
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // 6. Generar access token JWT
            String accessToken = jwtUtil.generateToken(userDetails);
            log.debug("Access token generado para usuario: {}", usuario.getUsuario());
            
            // 7. Obtener tiempo de expiración del token (en segundos)
            Long expirationMs = jwtUtil.extractExpiration(accessToken).getTime() - System.currentTimeMillis();
            Integer expiresIn = (int) (expirationMs / 1000);
            
            // 8. Crear refresh token con información del dispositivo
            String userAgent = request.getHeader("User-Agent");
            String ipAddress = request.getRemoteAddr();
            
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(
                usuario,
                userAgent,
                ipAddress
            );
            log.debug("Refresh token creado para usuario: {}", usuario.getUsuario());
            
            // 9. Construir respuesta con tokens e información del usuario
            LoginResponseDTO response = LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .usuario(mapToUsuarioInfo(usuario))
                .build();
            
            log.info("Login exitoso para usuario: {}", usuario.getUsuario());
            return response;
            
        } catch (BadCredentialsException e) {
            log.error("Credenciales inválidas para usuario: {}", loginRequest.username());
            throw new BadCredentialsException("Credenciales inválidas", e);
            
        } catch (DisabledException e) {
            log.error("Usuario inactivo: {}", loginRequest.username());
            throw e;
            
        } catch (AuthenticationException e) {
            log.error("Error de autenticación para usuario: {}", loginRequest.username(), e);
            throw new BadCredentialsException("Error de autenticación: " + e.getMessage(), e);
        }
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p><b>Implementación:</b>
     * <ol>
     *   <li>Busca el refresh token en la base de datos</li>
     *   <li>Valida que el token exista</li>
     *   <li>Verifica que el token no esté expirado</li>
     *   <li>Verifica que el token no esté revocado</li>
     *   <li>Genera un nuevo access token JWT</li>
     *   <li>Retorna el nuevo access token (mantiene el mismo refresh token)</li>
     * </ol>
     * 
     * <p><b>Nota:</b> El refresh token no se renueva, solo el access token.
     * Cuando el refresh token expire, el usuario deberá volver a hacer login.
     * 
     * @throws BadCredentialsException si el token es inválido, expirado o revocado
     */
    @Override
    @Transactional
    public LoginResponseDTO refreshAccessToken(String tokenValue) {
        log.info("Renovando access token");
        
        // 1. Buscar el refresh token
        RefreshToken refreshToken = refreshTokenService.findByToken(tokenValue)
            .orElseThrow(() -> {
                log.error("Refresh token no encontrado");
                return new BadCredentialsException("Token inválido");
            });
        
        // 2. Validar que el token sea válido (no expirado ni revocado)
        if (!refreshTokenService.isTokenValid(refreshToken)) {
            log.warn("Intento de renovación con token inválido para usuario: {}", 
                    refreshToken.getUsuario().getUsuario());
            throw new BadCredentialsException("Token expirado o revocado");
        }
        
        // 3. Obtener el usuario asociado al token
        Usuario usuario = refreshToken.getUsuario();
        
        // 4. Validar que el usuario siga activo
        if (!usuario.getActivo()) {
            log.warn("Intento de renovación con usuario inactivo: {}", usuario.getUsuario());
            throw new DisabledException("Usuario inactivo");
        }
        
        // 5. Crear CustomUserDetails para generar el nuevo token
        CustomUserDetails userDetails = new CustomUserDetails(
            usuario.getId(),
            usuario.getUsuario(),
            usuario.getPassword(),
            usuario.getNombreCompleto(),
            usuario.getPersona().getCorreo(),
            usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre()))
                .collect(Collectors.toCollection(HashSet::new))
        );
        
        // 6. Generar nuevo access token
        String newAccessToken = jwtUtil.generateToken(userDetails);
        log.debug("Nuevo access token generado para usuario: {}", usuario.getUsuario());
        
        // 7. Calcular tiempo de expiración
        Long expirationMs = jwtUtil.extractExpiration(newAccessToken).getTime() - System.currentTimeMillis();
        Integer expiresIn = (int) (expirationMs / 1000);
        
        // 8. Construir respuesta (mismo refresh token, nuevo access token)
        LoginResponseDTO response = LoginResponseDTO.builder()
            .accessToken(newAccessToken)
            .refreshToken(tokenValue) // Mismo refresh token
            .tokenType("Bearer")
            .expiresIn(expiresIn)
            .usuario(mapToUsuarioInfo(usuario))
            .build();
        
        log.info("Access token renovado exitosamente para usuario: {}", usuario.getUsuario());
        return response;
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p><b>Implementación:</b>
     * <ol>
     *   <li>Busca el refresh token en la base de datos</li>
     *   <li>Si existe, lo marca como revocado</li>
     *   <li>El token revocado no puede ser usado para renovar access tokens</li>
     * </ol>
     * 
     * <p><b>Importante:</b> El access token seguirá siendo válido hasta su
     * expiración. El cliente debe eliminar ambos tokens de su almacenamiento.
     */
    @Override
    @Transactional
    public void logout(String tokenValue) {
        log.info("Cerrando sesión");
        
        refreshTokenService.findByToken(tokenValue)
            .ifPresentOrElse(
                token -> {
                    refreshTokenService.revokeToken(token);
                    log.info("Sesión cerrada exitosamente para usuario: {}", 
                            token.getUsuario().getUsuario());
                },
                () -> log.warn("Intento de logout con token no encontrado")
            );
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p><b>Implementación:</b>
     * Obtiene el usuario autenticado del SecurityContextHolder de Spring Security.
     * Este método es útil para obtener información del usuario actual en
     * cualquier punto de la aplicación.
     * 
     * @throws org.springframework.security.core.AuthenticationException
     *         si no hay usuario autenticado en el contexto
     */
    @Override
    public UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("No hay usuario autenticado en el contexto");
            throw new BadCredentialsException("No hay usuario autenticado");
        }
        
        Object principal = authentication.getPrincipal();
        
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            log.debug("Usuario actual obtenido: {}", userDetails.getUsername());
            return userDetails;
        }
        
        log.error("El principal no es una instancia de UserDetails");
        throw new BadCredentialsException("Tipo de principal inválido");
    }
    
    /**
     * Mapea una entidad Usuario a un DTO de información de usuario.
     * 
     * <p>Este método privado convierte la información del usuario
     * en un formato apropiado para enviar al cliente, incluyendo
     * solo los datos necesarios y seguros.
     * 
     * @param usuario Entidad del usuario a mapear
     * @return DTO con información básica del usuario
     */
    private UsuarioInfoDTO mapToUsuarioInfo(Usuario usuario) {
        return UsuarioInfoDTO.builder()
            .id(usuario.getId())
            .usuario(usuario.getUsuario())
            .nombreCompleto(usuario.getNombreCompleto())
            .correo(usuario.getPersona().getCorreo())
            .roles(usuario.getRoles().stream()
                .map(Rol::getNombre)
                .collect(Collectors.toSet()))
            .build();
    }
}
