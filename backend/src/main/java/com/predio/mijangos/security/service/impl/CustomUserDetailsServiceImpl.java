package com.predio.mijangos.security.service.impl;

import com.predio.mijangos.modules.usuarios.domain.Rol;
import com.predio.mijangos.modules.usuarios.domain.Usuario;
import com.predio.mijangos.modules.usuarios.repository.UsuarioRepository;
import com.predio.mijangos.security.model.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementación personalizada del servicio de detalles de usuario.
 * 
 * <p>Este servicio es el punto de integración entre Spring Security
 * y la base de datos de usuarios del sistema. Se encarga de cargar
 * la información del usuario durante el proceso de autenticación.
 * 
 * <p><b>Responsabilidades:</b>
 * <ul>
 *   <li>Cargar usuario por username desde la base de datos</li>
 *   <li>Convertir entidad Usuario a CustomUserDetails de Spring Security</li>
 *   <li>Cargar y construir los roles y permisos del usuario</li>
 *   <li>Verificar el estado del usuario (activo/inactivo/eliminado)</li>
 *   <li>Proporcionar información de autenticación y autorización</li>
 * </ul>
 * 
 * <p><b>Convenciones de seguridad:</b>
 * <ul>
 *   <li>Los roles se prefijan con "ROLE_" (ej: ROLE_ADMIN, ROLE_VENDEDOR)</li>
 *   <li>Los permisos se mantienen sin prefijo (ej: PRODUCTO:READ, VENTA:WRITE)</li>
 *   <li>Usuarios inactivos lanzan DisabledException</li>
 *   <li>Usuarios eliminados (soft delete) lanzan UsernameNotFoundException</li>
 * </ul>
 * 
 * <p><b>CustomUserDetails vs User:</b><br>
 * Se retorna {@link CustomUserDetails} en lugar de {@link org.springframework.security.core.userdetails.User}
 * para incluir información adicional del usuario (ID, nombre completo, correo).
 * Esto es especialmente útil para:
 * <ul>
 *   <li>Auditoría automática (createdBy, updatedBy)</li>
 *   <li>Evitar consultas adicionales a la BD en cada request</li>
 *   <li>Personalización de la UI con datos del usuario</li>
 * </ul>
 * 
 * <p><b>Integración con Spring Security:</b><br>
 * Este servicio es llamado automáticamente por Spring Security durante
 * el proceso de autenticación cuando se configura como UserDetailsService.
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 * 
 * @see org.springframework.security.core.userdetails.UserDetailsService
 * @see org.springframework.security.core.userdetails.UserDetails
 * @see CustomUserDetails
 * @see com.predio.mijangos.modules.usuarios.domain.Usuario
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Carga un usuario por su username (código de usuario).
     * 
     * <p>Este método es el punto de entrada principal para Spring Security
     * durante el proceso de autenticación. Realiza las siguientes validaciones:
     * 
     * <ol>
     *   <li>Busca el usuario en la base de datos con sus roles</li>
     *   <li>Verifica que el usuario no esté eliminado (soft delete)</li>
     *   <li>Verifica que el usuario esté activo</li>
     *   <li>Construye las authorities (roles y permisos)</li>
     *   <li>Retorna un CustomUserDetails con toda la información</li>
     * </ol>
     * 
     * <p><b>Proceso de carga de roles:</b><br>
     * El sistema utiliza fetch join para cargar los roles del usuario
     * en una sola consulta, evitando el problema N+1. Los roles se
     * convierten a GrantedAuthority con el prefijo "ROLE_".
     * 
     * <p><b>Estados del usuario:</b>
     * <ul>
     *   <li><b>Usuario no encontrado:</b> UsernameNotFoundException</li>
     *   <li><b>Usuario eliminado (soft delete):</b> UsernameNotFoundException</li>
     *   <li><b>Usuario inactivo:</b> DisabledException</li>
     *   <li><b>Usuario activo:</b> CustomUserDetails con authorities cargadas</li>
     * </ul>
     * 
     * <p><b>Diferencia con versión anterior:</b><br>
     * Ahora retorna {@link CustomUserDetails} que incluye:
     * <ul>
     *   <li>ID del usuario (para auditoría)</li>
     *   <li>Nombre completo (para mostrar en UI)</li>
     *   <li>Correo electrónico (para notificaciones)</li>
     * </ul>
     * 
     * @param username Código de usuario (único en el sistema)
     * @return CustomUserDetails con la información del usuario y sus authorities
     * @throws UsernameNotFoundException Si el usuario no existe o está eliminado
     * @throws DisabledException Si el usuario existe pero está inactivo
     * 
     * @see #buildAuthorities(Usuario)
     * @see CustomUserDetails
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Cargando detalles de usuario: {}", username);
        
        // 1. Buscar usuario con sus roles (fetch join para evitar N+1)
        Usuario usuario = usuarioRepository.findByUsuarioWithRoles(username)
            .orElseThrow(() -> {
                log.error("Usuario no encontrado: {}", username);
                return new UsernameNotFoundException(
                    "Usuario no encontrado: " + username
                );
            });
        
        log.debug("Usuario encontrado: {}, Activo: {}, Eliminado: {}", 
                 username, usuario.getActivo(), usuario.isDeleted());

        // 2. Validar que el usuario no esté eliminado (soft delete)
        if (usuario.isDeleted()) {
            log.warn("Intento de acceso con usuario eliminado: {}", username);
            throw new UsernameNotFoundException(
                "Usuario no encontrado: " + username
            );
        }

        // 3. Validar que el usuario esté activo
        if (!usuario.getActivo()) {
            log.warn("Intento de acceso con usuario inactivo: {}", username);
            throw new DisabledException(
                "Usuario inactivo: " + username
            );
        }

        // 4. Construir authorities (roles y permisos)
        Set<GrantedAuthority> authorities = buildAuthorities(usuario);
        
        log.debug("Authorities cargadas para usuario {}: {}", 
                 username, authorities.size());

        // 5. Construir y retornar CustomUserDetails (en lugar de User)
        CustomUserDetails userDetails = new CustomUserDetails(
            usuario.getId(),                        // ID para auditoría
            usuario.getUsuario(),                   // Username
            usuario.getPassword(),                  // Password encriptado
            usuario.getNombreCompleto(),            // Nombre completo
            usuario.getPersona().getCorreo(),       // Correo electrónico
            authorities                             // Roles y permisos
        );
        
        log.info("CustomUserDetails construido exitosamente para usuario: {} (ID: {})", 
                username, usuario.getId());
        
        return userDetails;
    }

    /**
     * Construye el conjunto de authorities (roles y permisos) del usuario.
     * 
     * <p>Este método convierte los roles del usuario en GrantedAuthority
     * de Spring Security, aplicando las siguientes reglas:
     * 
     * <ul>
     *   <li><b>Roles:</b> Se prefijan con "ROLE_" (ej: ROLE_ADMIN)</li>
     *   <li><b>Formato:</b> Nombres en mayúsculas</li>
     *   <li><b>Validación:</b> Ignora roles nulos o sin nombre</li>
     * </ul>
     * 
     * <p><b>Ejemplo de conversión:</b>
     * <pre>
     * Rol en BD: "ADMIN" → Authority: "ROLE_ADMIN"
     * Rol en BD: "VENDEDOR" → Authority: "ROLE_VENDEDOR"
     * </pre>
     * 
     * <p><b>Nota sobre permisos:</b><br>
     * Actualmente solo se cargan roles. Si en el futuro se implementan
     * permisos granulares a nivel de página/acción, este método debe
     * extenderse para incluirlos sin el prefijo "ROLE_".
     * 
     * @param usuario Entidad del usuario con sus roles cargados
     * @return Set de GrantedAuthority con roles convertidos
     * 
     * @see org.springframework.security.core.GrantedAuthority
     * @see org.springframework.security.core.authority.SimpleGrantedAuthority
     */
    private Set<GrantedAuthority> buildAuthorities(Usuario usuario) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        
        // Validar que el usuario tenga roles
        if (usuario.getRoles() == null || usuario.getRoles().isEmpty()) {
            log.warn("Usuario {} no tiene roles asignados", usuario.getUsuario());
            return authorities;
        }
        
        // Convertir roles a authorities con prefijo "ROLE_"
        authorities = usuario.getRoles().stream()
            .filter(rol -> rol != null && rol.getNombre() != null)
            .map(rol -> {
                String authority = "ROLE_" + rol.getNombre().toUpperCase();
                log.trace("Agregando authority: {}", authority);
                return new SimpleGrantedAuthority(authority);
            })
            .collect(Collectors.toSet());
        
        log.debug("Total authorities construidas: {}", authorities.size());
        return authorities;
    }

    /**
     * Carga un usuario por su ID.
     * 
     * <p>Método de utilidad para cargar un usuario cuando ya se tiene su ID.
     * Útil para recargar información del usuario desde el token JWT o
     * para operaciones que requieren el CustomUserDetails completo.
     * 
     * <p><b>Uso típico:</b>
     * <ul>
     *   <li>Recargar información después de actualizar roles</li>
     *   <li>Validar permisos en operaciones específicas</li>
     *   <li>Obtener UserDetails desde un ID almacenado en token</li>
     * </ul>
     * 
     * @param userId ID del usuario a cargar
     * @return CustomUserDetails del usuario
     * @throws UsernameNotFoundException Si el usuario no existe
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Integer userId) {
        log.debug("Cargando usuario por ID: {}", userId);
        
        Usuario usuario = usuarioRepository.findByIdWithRoles(userId)
            .orElseThrow(() -> {
                log.error("Usuario no encontrado con ID: {}", userId);
                return new UsernameNotFoundException(
                    "Usuario no encontrado con ID: " + userId
                );
            });
        
        // Reutilizar el método principal de carga
        return loadUserByUsername(usuario.getUsuario());
    }
}
