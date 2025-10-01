package com.predio.mijangos.security;

import com.predio.mijangos.modules.security.domain.Usuario;
import com.predio.mijangos.modules.security.repo.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Carga usuario del dominio y lo adapta a UserDetails (DIP).
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UsuarioRepository usuarioRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Usuario u = usuarioRepository.findByUsuario(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    String[] authorities = u.getRoles().stream()
        .map(r -> "ROLE_" + r.getNombre())
        .toArray(String[]::new);

    return User.withUsername(u.getUsuario())
        .password(u.getPassword())
        .authorities(authorities)
        .disabled(Boolean.FALSE.equals(u.getActivo()))
        .build();
  }
}
