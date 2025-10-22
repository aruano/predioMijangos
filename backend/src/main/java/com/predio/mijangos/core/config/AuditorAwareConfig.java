package com.predio.mijangos.core.config;

import com.predio.mijangos.security.model.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Configuración de auditoría automática con JPA.
 * 
 * <p>Habilita el llenado automático de campos de auditoría en todas
 * las entidades que extienden {@link com.predio.mijangos.core.entity.BaseEntity}:
 * 
 * <ul>
 *   <li><b>createdBy:</b> ID del usuario que creó el registro</li>
 *   <li><b>updatedBy:</b> ID del usuario que actualizó el registro</li>
 *   <li><b>createdAt:</b> Timestamp de creación (automático con @CreatedDate)</li>
 *   <li><b>updatedAt:</b> Timestamp de última actualización (automático con @LastModifiedDate)</li>
 * </ul>
 * 
 * <p><b>Funcionamiento:</b><br>
 * Spring Data JPA llama al {@link AuditorAware#getCurrentAuditor()} automáticamente:
 * <ul>
 *   <li>Antes de persistir una nueva entidad (para llenar createdBy)</li>
 *   <li>Antes de actualizar una entidad existente (para llenar updatedBy)</li>
 * </ul>
 * 
 * <p><b>Requisitos:</b>
 * <ul>
 *   <li>El usuario debe estar autenticado</li>
 *   <li>El principal debe ser una instancia de {@link CustomUserDetails}</li>
 *   <li>La entidad debe extender {@link com.predio.mijangos.core.entity.BaseEntity}</li>
 * </ul>
 * 
 * <p><b>Ventajas de usar CustomUserDetails:</b>
 * <ul>
 *   <li>No requiere consultas adicionales a la BD para obtener el ID</li>
 *   <li>El ID del usuario está disponible en el contexto de seguridad</li>
 *   <li>Evita overhead de performance en cada operación de auditoría</li>
 * </ul>
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 * 
 * @see com.predio.mijangos.core.entity.BaseEntity
 * @see CustomUserDetails
 * @see com.predio.mijangos.security.service.impl.CustomUserDetailsServiceImpl
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@Slf4j
public class AuditorAwareConfig {

    /**
     * Proporciona el ID del usuario actual autenticado para auditoría.
     * 
     * <p>Este bean es invocado automáticamente por Spring Data JPA cuando:
     * <ol>
     *   <li>Se crea una nueva entidad (para llenar createdBy)</li>
     *   <li>Se actualiza una entidad existente (para llenar updatedBy)</li>
     * </ol>
     * 
     * <p><b>Proceso de obtención del auditor:</b>
     * <ol>
     *   <li>Obtiene el contexto de seguridad de Spring Security</li>
     *   <li>Extrae la autenticación actual</li>
     *   <li>Verifica que haya un usuario autenticado</li>
     *   <li>Verifica que no sea un usuario anónimo</li>
     *   <li>Extrae el ID del usuario desde CustomUserDetails</li>
     *   <li>Retorna el ID como Optional</li>
     * </ol>
     * 
     * <p><b>Casos especiales:</b>
     * <ul>
     *   <li><b>Sin autenticación:</b> Retorna Optional.empty() → createdBy/updatedBy = null</li>
     *   <li><b>Usuario anónimo:</b> Retorna Optional.empty() → createdBy/updatedBy = null</li>
     *   <li><b>Principal no es CustomUserDetails:</b> Retorna Optional.empty() → createdBy/updatedBy = null</li>
     *   <li><b>Error inesperado:</b> Retorna Optional.empty() y logea el error</li>
     * </ul>
     * 
     * <p><b>Ejemplo de uso automático:</b>
     * <pre>
     * // Al guardar una entidad, Spring Data JPA automáticamente:
     * Usuario usuario = new Usuario();
     * usuario.setUsuario("ADMIN");
     * usuarioRepository.save(usuario);
     * 
     * // Spring Data JPA llama a auditorProvider() y establece:
     * // usuario.createdBy = [ID del usuario autenticado]
     * // usuario.createdAt = [timestamp actual]
     * </pre>
     * 
     * <p><b>Nota de performance:</b><br>
     * Este método se ejecuta en cada operación de persistencia/actualización,
     * por lo que debe ser muy eficiente. Al usar CustomUserDetails, el ID
     * ya está en memoria y no requiere consultas adicionales a la BD.
     * 
     * @return Optional con el ID del usuario autenticado, o Optional.empty() si no hay usuario
     * 
     * @see CustomUserDetails#getId()
     * @see org.springframework.security.core.context.SecurityContextHolder
     */
    @Bean
    public AuditorAware<Integer> auditorProvider() {
        return () -> {
            // 1. Obtener autenticación del contexto de seguridad
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            // 2. Verificar que haya autenticación y que el usuario esté autenticado
            if (authentication == null || !authentication.isAuthenticated()) {
                log.trace("No hay autenticación activa, auditoría omitida");
                return Optional.empty();
            }
            
            // 3. Obtener el principal (usuario autenticado)
            Object principal = authentication.getPrincipal();
            
            // 4. Verificar que no sea un usuario anónimo
            if ("anonymousUser".equals(principal)) {
                log.trace("Usuario anónimo detectado, auditoría omitida");
                return Optional.empty();
            }
            
            try {
                // 5. Verificar que el principal sea CustomUserDetails
                if (principal instanceof CustomUserDetails) {
                    CustomUserDetails userDetails = (CustomUserDetails) principal;
                    Integer userId = userDetails.getId();
                    
                    log.trace("Auditor identificado: Usuario ID {}", userId);
                    return Optional.of(userId);
                }
                
                // 6. Si el principal no es CustomUserDetails, logear advertencia
                log.warn("El principal no es una instancia de CustomUserDetails: {}. " +
                        "Verifica que CustomUserDetailsServiceImpl esté retornando CustomUserDetails.",
                        principal.getClass().getSimpleName());
                return Optional.empty();
                
            } catch (Exception e) {
                // 7. En caso de error inesperado, logear y retornar vacío
                log.error("Error al obtener el auditor: {}", e.getMessage(), e);
                return Optional.empty();
            }
        };
    }
}
