package com.predio.mijangos.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Servicio personalizado para cargar detalles de usuarios desde la base de datos.
 * 
 * Implementa UserDetailsService de Spring Security para integración con
 * el sistema de autenticación.
 * 
 * Responsabilidades:
 * - Cargar usuario por username (código de empleado)
 * - Convertir entidad Usuario a UserDetails de Spring Security
 * - Cargar roles y permisos del usuario
 * - Verificar estado del usuario (activo/inactivo)
 * 
 * NOTA: Este servicio necesita acceso al UsuarioRepository.
 * Debes inyectar el repository cuando implementes el módulo de usuarios.
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    // TODO: Inyectar UsuarioRepository cuando esté disponible
    // private final UsuarioRepository usuarioRepository;

    /**
     * Carga un usuario por su username (código de empleado).
     * 
     * Este método es llamado automáticamente por Spring Security durante
     * el proceso de autenticación.
     * 
     * @param username Código de empleado del usuario
     * @return UserDetails con la información del usuario y sus roles
     * @throws UsernameNotFoundException Si el usuario no existe
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Cargando usuario con username: {}", username);

        // TODO: Implementar cuando UsuarioRepository esté disponible
        /*
        Usuario usuario = usuarioRepository.findByCodigoEmpleado(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con código: " + username
                ));
        
        // Verificar que el usuario esté activo
        if (usuario.getDeletedAt() != null) {
            throw new UsernameNotFoundException("Usuario inactivo: " + username);
        }
        
        if (!usuario.getActivo()) {
            throw new UsernameNotFoundException("Usuario deshabilitado: " + username);
        }
        
        // Convertir a UserDetails
        return buildUserDetails(usuario);
        */

        // IMPLEMENTACIÓN TEMPORAL PARA TESTING
        // Reemplazar con código real cuando el módulo de usuarios esté listo
        return buildTemporaryUserForTesting(username);
    }

    /**
     * Construye un objeto UserDetails desde una entidad Usuario.
     * 
     * @param usuario Entidad Usuario de la base de datos
     * @return UserDetails para Spring Security
     */
    /*
    private UserDetails buildUserDetails(Usuario usuario) {
        Collection<? extends GrantedAuthority> authorities = buildAuthorities(usuario);
        
        return User.builder()
                .username(usuario.getCodigoEmpleado())
                .password(usuario.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!usuario.getActivo())
                .build();
    }
    */

    /**
     * Construye las authorities (roles y permisos) del usuario.
     * 
     * En este sistema:
     * - Los roles se prefijan con "ROLE_" (ej: ROLE_ADMIN)
     * - Los permisos se mantienen sin prefijo (ej: PRODUCTO:WRITE)
     * 
     * @param usuario Entidad Usuario
     * @return Colección de GrantedAuthority
     */
    /*
    private Collection<? extends GrantedAuthority> buildAuthorities(Usuario usuario) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        
        // Agregar roles
        if (usuario.getRoles() != null) {
            usuario.getRoles().forEach(rol -> {
                // Agregar el rol
                authorities.add(new SimpleGrantedAuthority("ROLE_" + rol.getNombre()));
                
                // Agregar permisos del rol
                if (rol.getPermisos() != null) {
                    rol.getPermisos().forEach(permiso ->
                            authorities.add(new SimpleGrantedAuthority(permiso.getCodigo()))
                    );
                }
            });
        }
        
        return authorities;
    }
    */

    /**
     * MÉTODO TEMPORAL PARA TESTING
     * Crea un usuario de prueba para desarrollo.
     * 
     * ⚠️ ELIMINAR ESTE MÉTODO cuando el módulo de usuarios esté implementado.
     */
    private UserDetails buildTemporaryUserForTesting(String username) {
        log.warn("⚠️ USANDO IMPLEMENTACIÓN TEMPORAL - Reemplazar con código real");
        
        // Usuario de prueba con contraseña "password123"
        // Password hash generado con BCryptPasswordEncoder(12)
        return User.builder()
                .username(username)
                .password("$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5lWjy/K5glqK.") // password123
                .authorities(
                        new SimpleGrantedAuthority("ROLE_ADMIN"),
                        new SimpleGrantedAuthority("ROLE_OPERADOR"),
                        new SimpleGrantedAuthority("PRODUCTO:READ"),
                        new SimpleGrantedAuthority("PRODUCTO:WRITE"),
                        new SimpleGrantedAuthority("VENTA:READ"),
                        new SimpleGrantedAuthority("VENTA:WRITE")
                )
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    /**
     * Carga un usuario por su ID.
     * Útil cuando ya tienes el ID del usuario y necesitas recargar sus datos.
     * 
     * @param userId ID del usuario
     * @return UserDetails del usuario
     * @throws UsernameNotFoundException Si el usuario no existe
     */
    /*
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Integer userId) {
        log.debug("Cargando usuario con ID: {}", userId);
        
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con ID: " + userId
                ));
        
        return buildUserDetails(usuario);
    }
    */
}