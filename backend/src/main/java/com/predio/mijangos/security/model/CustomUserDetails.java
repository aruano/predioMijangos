package com.predio.mijangos.security.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Extensión personalizada de UserDetails de Spring Security.
 * 
 * <p>Extiende {@link User} de Spring Security para incluir información
 * adicional del usuario que es útil en toda la aplicación sin necesidad
 * de consultas adicionales a la base de datos.
 * 
 * <p><b>Información adicional incluida:</b>
 * <ul>
 *   <li><b>id:</b> ID único del usuario (Integer) - Usado para auditoría</li>
 *   <li><b>nombreCompleto:</b> Nombre completo del usuario - Para mostrar en UI</li>
 *   <li><b>correo:</b> Correo electrónico - Para notificaciones y contacto</li>
 * </ul>
 * 
 * <p><b>Ventajas sobre User estándar:</b>
 * <ul>
 *   <li>Auditoría automática: El ID se usa en createdBy/updatedBy sin consultas extra</li>
 *   <li>Performance: Datos del usuario en memoria, sin necesidad de recargar</li>
 *   <li>Personalización UI: Nombre completo y correo disponibles en cada request</li>
 *   <li>Reducción de acoplamiento: No necesitas inyectar UsuarioRepository en cada clase</li>
 * </ul>
 * 
 * <p><b>Uso típico:</b>
 * <pre>
 * // En un controller o service
 * &#64;GetMapping("/perfil")
 * public PerfilDTO obtenerPerfil(&#64;AuthenticationPrincipal CustomUserDetails userDetails) {
 *     return PerfilDTO.builder()
 *         .id(userDetails.getId())
 *         .username(userDetails.getUsername())
 *         .nombreCompleto(userDetails.getNombreCompleto())
 *         .correo(userDetails.getCorreo())
 *         .roles(userDetails.getAuthorities())
 *         .build();
 * }
 * </pre>
 * 
 * <p><b>Integración con Spring Security:</b><br>
 * Esta clase es retornada por {@link com.predio.mijangos.security.service.impl.CustomUserDetailsServiceImpl}
 * durante el proceso de autenticación y queda disponible en el SecurityContext
 * durante toda la sesión del usuario.
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 * 
 * @see org.springframework.security.core.userdetails.User
 * @see org.springframework.security.core.userdetails.UserDetails
 * @see com.predio.mijangos.security.service.impl.CustomUserDetailsServiceImpl
 * @see com.predio.mijangos.core.config.AuditorAwareConfig
 */
@Getter
public class CustomUserDetails extends User {
    
    /**
     * ID único del usuario en la base de datos.
     * Utilizado principalmente para auditoría automática (createdBy, updatedBy).
     */
    private final Integer id;
    
    /**
     * Nombre completo del usuario.
     * Útil para mostrar en la interfaz sin necesidad de consultas adicionales.
     */
    private final String nombreCompleto;
    
    /**
     * Correo electrónico del usuario.
     * Utilizado para notificaciones, recuperación de contraseña y contacto.
     */
    private final String correo;

    /**
     * Constructor principal para crear CustomUserDetails.
     * 
     * <p>Crea un usuario con configuración predeterminada:
     * <ul>
     *   <li>enabled = true</li>
     *   <li>accountNonExpired = true</li>
     *   <li>credentialsNonExpired = true</li>
     *   <li>accountNonLocked = true</li>
     * </ul>
     * 
     * <p>Este constructor es el más usado en la aplicación para
     * autenticación estándar de usuarios activos.
     * 
     * @param id ID único del usuario en la base de datos
     * @param username Código de usuario (ej: "ADMIN", "VEND001")
     * @param password Contraseña encriptada con BCrypt
     * @param nombreCompleto Nombre completo del usuario
     * @param correo Correo electrónico del usuario
     * @param authorities Roles y permisos del usuario
     */
    public CustomUserDetails(
            Integer id,
            String username,
            String password,
            String nombreCompleto,
            String correo,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
    }

    /**
     * Constructor completo con control granular de flags de cuenta.
     * 
     * <p>Permite especificar individualmente cada flag de estado de la cuenta:
     * <ul>
     *   <li><b>enabled:</b> Si la cuenta está habilitada</li>
     *   <li><b>accountNonExpired:</b> Si la cuenta no ha expirado</li>
     *   <li><b>credentialsNonExpired:</b> Si las credenciales no han expirado</li>
     *   <li><b>accountNonLocked:</b> Si la cuenta no está bloqueada</li>
     * </ul>
     * 
     * <p><b>Uso típico:</b><br>
     * Este constructor se usa en escenarios avanzados donde se requiere
     * control fino sobre el estado de la cuenta, como:
     * <ul>
     *   <li>Implementación de bloqueo por intentos fallidos</li>
     *   <li>Expiración de cuentas temporales</li>
     *   <li>Expiración de contraseñas por políticas de seguridad</li>
     * </ul>
     * 
     * @param id ID único del usuario en la base de datos
     * @param username Código de usuario (ej: "ADMIN", "VEND001")
     * @param password Contraseña encriptada con BCrypt
     * @param enabled Si la cuenta está habilitada (activo = true)
     * @param accountNonExpired Si la cuenta no ha expirado temporalmente
     * @param credentialsNonExpired Si la contraseña no ha expirado
     * @param accountNonLocked Si la cuenta no está bloqueada por seguridad
     * @param nombreCompleto Nombre completo del usuario
     * @param correo Correo electrónico del usuario
     * @param authorities Roles y permisos del usuario
     */
    public CustomUserDetails(
            Integer id,
            String username,
            String password,
            boolean enabled,
            boolean accountNonExpired,
            boolean credentialsNonExpired,
            boolean accountNonLocked,
            String nombreCompleto,
            String correo,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
    }
}
