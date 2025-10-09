package com.predio.mijangos.security.service;

import com.predio.mijangos.core.exception.BusinessException;
import com.predio.mijangos.core.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Servicio de autenticación para gestionar login, registro y tokens.
 * 
 * Proporciona métodos para:
 * - Autenticar usuarios (login)
 * - Registrar nuevos usuarios
 * - Generar tokens JWT
 * - Refrescar tokens
 * - Cerrar sesión
 * - Obtener usuario actual
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    
    // TODO: Inyectar UsuarioRepository cuando esté disponible
    // private final UsuarioRepository usuarioRepository;

    /**
     * Autentica un usuario y genera tokens de acceso y refresh.
     * 
     * @param username Código de empleado del usuario
     * @param password Contraseña del usuario
     * @return Mapa con accessToken, refreshToken, tokenType y expiresIn
     * @throws BadCredentialsException Si las credenciales son inválidas
     */
    @Transactional
    public Map<String, Object> login(String username, String password) {
        log.info("Intento de login para usuario: {}", username);
        
        try {
            // 1. Autenticar con Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            
            // 2. Establecer autenticación en el contexto
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // 3. Cargar detalles del usuario
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            // 4. Generar tokens
            String accessToken = jwtUtil.generateToken(userDetails);
            String refreshToken = refreshTokenService.createRefreshToken(username);
            
            // 5. Construir respuesta
            Map<String, Object> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            tokens.put("tokenType", "Bearer");
            tokens.put("expiresIn", 28800); // 8 horas en segundos
            
            // 6. Información adicional del usuario
            tokens.put("username", username);
            tokens.put("roles", userDetails.getAuthorities().stream()
                    .map(auth -> auth.getAuthority())
                    .filter(auth -> auth.startsWith("ROLE_"))
                    .map(auth -> auth.substring(5)) // Remover prefijo "ROLE_"
                    .toList());
            
            log.info("Login exitoso para usuario: {}", username);
            return tokens;
            
        } catch (BadCredentialsException ex) {
            log.warn("Credenciales inválidas para usuario: {}", username);
            throw new BadCredentialsException("Código de empleado o contraseña incorrectos");
        } catch (Exception ex) {
            log.error("Error durante el login", ex);
            throw new BusinessException("Error durante el proceso de autenticación");
        }
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * 
     * NOTA: Este método debe ser protegido con permisos apropiados.
     * Solo ADMIN debería poder registrar nuevos usuarios.
     * 
     * @param username Código de empleado
     * @param password Contraseña
     * @param email Email del usuario
     * @param nombreCompleto Nombre completo del usuario
     * @return Datos del usuario registrado
     */
    /*
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponseDTO register(String username, String password, String email, String nombreCompleto) {
        log.info("Registrando nuevo usuario: {}", username);
        
        // 1. Verificar que el código de empleado no exista
        if (usuarioRepository.existsByCodigoEmpleado(username)) {
            throw new BusinessException("DUPLICATE_USER", 
                    "Ya existe un usuario con el código de empleado: " + username);
        }
        
        // 2. Verificar que el email no exista
        if (usuarioRepository.existsByEmail(email)) {
            throw new BusinessException("DUPLICATE_EMAIL", 
                    "Ya existe un usuario con el email: " + email);
        }
        
        // 3. Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setCodigoEmpleado(username);
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setEmail(email);
        usuario.setNombreCompleto(nombreCompleto);
        usuario.setActivo(true);
        
        // 4. Asignar rol por defecto (OPERADOR)
        Rol rolOperador = rolRepository.findByNombre("OPERADOR")
                .orElseThrow(() -> new BusinessException("Rol OPERADOR no encontrado"));
        usuario.setRoles(Set.of(rolOperador));
        
        // 5. Guardar usuario
        Usuario savedUsuario = usuarioRepository.save(usuario);
        
        log.info("Usuario registrado exitosamente: {}", username);
        return usuarioMapper.toResponseDTO(savedUsuario);
    }
    */

    /**
     * Refresca un token de acceso usando un refresh token válido.
     * 
     * @param refreshToken Refresh token
     * @return Nuevo access token
     */
    @Transactional
    public Map<String, Object> refreshAccessToken(String refreshToken) {
        log.debug("Refrescando access token");
        
        // 1. Validar refresh token
        if (!refreshTokenService.validateRefreshToken(refreshToken)) {
            throw new BusinessException("INVALID_REFRESH_TOKEN", 
                    "Refresh token inválido o expirado");
        }
        
        // 2. Obtener username del refresh token
        String username = refreshTokenService.getUsernameFromRefreshToken(refreshToken);
        
        // 3. Cargar detalles del usuario
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        // 4. Generar nuevo access token
        String newAccessToken = jwtUtil.generateToken(userDetails);
        
        // 5. Construir respuesta
        Map<String, Object> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);
        tokens.put("tokenType", "Bearer");
        tokens.put("expiresIn", 28800); // 8 horas en segundos
        
        log.debug("Access token refrescado para usuario: {}", username);
        return tokens;
    }

    /**
     * Cierra la sesión del usuario invalidando su refresh token.
     * 
     * @param refreshToken Refresh token a invalidar
     */
    @Transactional
    public void logout(String refreshToken) {
        log.info("Cerrando sesión");
        
        if (refreshToken != null && !refreshToken.isEmpty()) {
            refreshTokenService.deleteRefreshToken(refreshToken);
        }
        
        // Limpiar contexto de seguridad
        SecurityContextHolder.clearContext();
        
        log.info("Sesión cerrada exitosamente");
    }

    /**
     * Obtiene el usuario actualmente autenticado.
     * 
     * @return UserDetails del usuario actual
     * @throws BusinessException Si no hay usuario autenticado
     */
    public UserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || 
                "anonymousUser".equals(authentication.getPrincipal())) {
            throw new BusinessException("NO_AUTHENTICATED_USER", 
                    "No hay usuario autenticado en el contexto");
        }
        
        return (UserDetails) authentication.getPrincipal();
    }

    /**
     * Obtiene el username del usuario actualmente autenticado.
     * 
     * @return Username del usuario actual
     * @throws BusinessException Si no hay usuario autenticado
     */
    public String getCurrentUsername() {
        return getCurrentUser().getUsername();
    }

    /**
     * Cambia la contraseña del usuario actual.
     * 
     * @param currentPassword Contraseña actual
     * @param newPassword Nueva contraseña
     */
    /*
    @Transactional
    public void changePassword(String currentPassword, String newPassword) {
        String username = getCurrentUsername();
        log.info("Cambiando contraseña para usuario: {}", username);
        
        // 1. Verificar contraseña actual
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!passwordEncoder.matches(currentPassword, userDetails.getPassword())) {
            throw new BusinessException("INVALID_CURRENT_PASSWORD", 
                    "La contraseña actual es incorrecta");
        }
        
        // 2. Actualizar contraseña
        Usuario usuario = usuarioRepository.findByCodigoEmpleado(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", username));
        
        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
        
        // 3. Invalidar todos los refresh tokens del usuario
        refreshTokenService.deleteAllUserRefreshTokens(username);
        
        log.info("Contraseña cambiada exitosamente para usuario: {}", username);
    }
    */

    /**
     * Valida si una contraseña cumple con los requisitos de seguridad.
     * 
     * Requisitos:
     * - Mínimo 8 caracteres
     * - Al menos una letra mayúscula
     * - Al menos una letra minúscula
     * - Al menos un número
     * - Al menos un carácter especial
     * 
     * @param password Contraseña a validar
     * @return true si cumple los requisitos, false en caso contrario
     */
    public boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        
        boolean hasUpperCase = password.matches(".*[A-Z].*");
        boolean hasLowerCase = password.matches(".*[a-z].*");
        boolean hasNumber = password.matches(".*\\d.*");
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*");
        
        return hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar;
    }
}