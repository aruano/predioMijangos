package com.predio.mijangos.security.service;

/**
 * NOTA: Este archivo existe solo por compatibilidad histórica.
 * 
 * La implementación real está en:
 * {@link com.predio.mijangos.security.service.impl.CustomUserDetailsServiceImpl}
 * 
 * No es necesaria una interfaz adicional ya que CustomUserDetailsServiceImpl
 * implementa directamente UserDetailsService de Spring Security.
 * 
 * @deprecated Use {@link com.predio.mijangos.security.service.impl.CustomUserDetailsServiceImpl} directamente
 * @see com.predio.mijangos.security.service.impl.CustomUserDetailsServiceImpl
 * @see org.springframework.security.core.userdetails.UserDetailsService
 */
@Deprecated(since = "1.0.0", forRemoval = true)
public interface CustomUserDetailsService {
    // Esta interfaz no debe ser usada.
    // La implementación correcta está en CustomUserDetailsServiceImpl
    // que implementa directamente UserDetailsService de Spring Security.
}
