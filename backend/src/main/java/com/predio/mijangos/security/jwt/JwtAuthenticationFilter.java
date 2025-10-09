package com.predio.mijangos.security.jwt;

import com.predio.mijangos.core.security.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de autenticación JWT que intercepta todas las requests HTTP.
 * 
 * Responsabilidades:
 * - Extraer el token JWT del header Authorization
 * - Validar el token
 * - Cargar los detalles del usuario
 * - Establecer la autenticación en el contexto de seguridad
 * 
 * El filtro se ejecuta una vez por request antes de llegar al controlador.
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    /**
     * Método principal del filtro que se ejecuta en cada request.
     * 
     * Flujo:
     * 1. Extrae el token del header Authorization
     * 2. Valida el formato y estructura del token
     * 3. Extrae el username del token
     * 4. Carga los detalles del usuario desde la BD
     * 5. Valida el token contra el usuario
     * 6. Establece la autenticación en el contexto de seguridad
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        try {
            // 1. Extraer token del header Authorization
            String jwt = extractJwtFromRequest(request);
            
            // 2. Si no hay token o el usuario ya está autenticado, continuar
            if (jwt == null) {
                log.debug("No se encontró token JWT en el request");
                filterChain.doFilter(request, response);
                return;
            }
            
            // 3. Validar estructura del token
            if (!jwtUtil.validateToken(jwt)) {
                log.warn("Token JWT inválido o expirado");
                filterChain.doFilter(request, response);
                return;
            }
            
            // 4. Extraer username del token
            String username = jwtUtil.extractUsername(jwt);
            
            // 5. Si el usuario no está autenticado en el contexto, autenticarlo
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // 6. Cargar detalles del usuario desde la base de datos
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // 7. Validar token contra el usuario cargado
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    
                    // 8. Crear token de autenticación de Spring Security
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    
                    // 9. Agregar detalles del request
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // 10. Establecer autenticación en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    log.debug("Usuario '{}' autenticado exitosamente", username);
                } else {
                    log.warn("Token JWT inválido para el usuario '{}'", username);
                }
            }
            
        } catch (Exception ex) {
            // No lanzar excepción aquí, solo logear
            // El manejo de errores se hace en JwtAuthenticationEntryPoint
            log.error("Error al procesar autenticación JWT: {}", ex.getMessage());
        }
        
        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT del header Authorization del request.
     * 
     * Formato esperado: "Authorization: Bearer {token}"
     * 
     * @param request HttpServletRequest
     * @return Token JWT sin el prefijo "Bearer ", o null si no existe
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remover "Bearer " del inicio
        }
        
        return null;
    }

    /**
     * Determina si este filtro debe ejecutarse para el request dado.
     * Por defecto, se ejecuta para todas las rutas excepto las públicas.
     * 
     * Puede sobrescribirse para excluir rutas específicas.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        
        // No filtrar rutas públicas de autenticación
        if (path.startsWith("/api/auth/")) {
            return true;
        }
        
        // No filtrar Swagger/OpenAPI
        if (path.startsWith("/swagger-ui") || 
            path.startsWith("/v3/api-docs") ||
            path.equals("/swagger-ui.html")) {
            return true;
        }
        
        // No filtrar Actuator health
        if (path.equals("/actuator/health")) {
            return true;
        }
        
        // Filtrar todas las demás rutas
        return false;
    }
}