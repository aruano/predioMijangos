package com.predio.mijangos.security.service;

import com.predio.mijangos.security.dto.LoginRequestDTO;
import com.predio.mijangos.security.dto.LoginResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Servicio de autenticación del sistema.
 * 
 * <p>Maneja todas las operaciones relacionadas con la autenticación de usuarios,
 * incluyendo login, renovación de tokens y cierre de sesión.
 * 
 * <p><b>Responsabilidades:</b>
 * <ul>
 *   <li>Autenticar usuarios con credenciales (usuario/contraseña)</li>
 *   <li>Generar access tokens JWT</li>
 *   <li>Generar y gestionar refresh tokens</li>
 *   <li>Renovar access tokens usando refresh tokens válidos</li>
 *   <li>Invalidar tokens al cerrar sesión</li>
 *   <li>Obtener información del usuario autenticado actual</li>
 * </ul>
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
public interface AuthenticationService {
    
    /**
     * Autentica un usuario con sus credenciales.
     * 
     * <p>Valida las credenciales del usuario y, si son correctas,
     * genera un access token JWT y un refresh token.
     * 
     * <p><b>Proceso:</b>
     * <ol>
     *   <li>Valida credenciales (usuario/contraseña)</li>
     *   <li>Verifica que el usuario esté activo</li>
     *   <li>Genera access token JWT</li>
     *   <li>Crea y persiste refresh token</li>
     *   <li>Retorna tokens e información del usuario</li>
     * </ol>
     * 
     * @param request DTO con username y password
     * @return DTO con access token, refresh token e información del usuario
     * @throws org.springframework.security.authentication.BadCredentialsException
     *         si las credenciales son inválidas
     * @throws org.springframework.security.authentication.DisabledException
     *         si el usuario está inactivo
     */
    LoginResponseDTO login(LoginRequestDTO request);
    
    /**
     * Renueva el access token usando un refresh token válido.
     * 
     * <p>Permite al cliente obtener un nuevo access token sin necesidad
     * de volver a autenticarse con usuario y contraseña, mejorando
     * la experiencia del usuario.
     * 
     * <p><b>Proceso:</b>
     * <ol>
     *   <li>Valida que el refresh token exista</li>
     *   <li>Verifica que no esté expirado</li>
     *   <li>Verifica que no esté revocado</li>
     *   <li>Genera nuevo access token</li>
     *   <li>Retorna nuevo access token (mismo refresh token)</li>
     * </ol>
     * 
     * @param refreshToken Token de refresco válido
     * @return DTO con nuevo access token e información del usuario
     * @throws org.springframework.security.authentication.BadCredentialsException
     *         si el refresh token es inválido o expirado
     */
    LoginResponseDTO refreshAccessToken(String refreshToken);
    
    /**
     * Cierra sesión del usuario invalidando su refresh token.
     * 
     * <p>Revoca el refresh token para que no pueda ser usado nuevamente.
     * El access token seguirá siendo válido hasta su expiración natural.
     * 
     * <p><b>Nota importante:</b> El cliente debe eliminar tanto el access
     * token como el refresh token de su almacenamiento local.
     * 
     * @param refreshToken Token de refresco a invalidar
     * @throws com.predio.mijangos.core.exception.ResourceNotFoundException
     *         si el refresh token no existe
     */
    void logout(String refreshToken);
    
    /**
     * Obtiene la información del usuario actualmente autenticado.
     * 
     * <p>Recupera los detalles del usuario desde el contexto de seguridad
     * de Spring Security.
     * 
     * @return UserDetails del usuario autenticado
     * @throws org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
     *         si no hay usuario autenticado en el contexto
     */
    UserDetails getCurrentUser();
}
