package com.predio.mijangos.modules.security.service.impl;

import com.predio.mijangos.modules.security.domain.Rol;
import com.predio.mijangos.modules.security.domain.Usuario;
import com.predio.mijangos.modules.security.dto.AuthRequestDTO;
import com.predio.mijangos.modules.security.dto.AuthResponseDTO;
import com.predio.mijangos.modules.security.repo.UsuarioRepository;
import com.predio.mijangos.modules.security.service.AuthService;
import com.predio.mijangos.modules.security.service.MenuService;
import com.predio.mijangos.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.AuthenticationException;

/**
 * Caso de uso de Login:
 *  - Autentica credenciales.
 *  - Genera JWT.
 *  - Construye menú (módulos/páginas) según roles del usuario.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final AuthenticationManager authenticationManager;
  private final UsuarioRepository usuarioRepository;
  private final JwtUtil jwtUtil;
  private final MenuService menuService;

  @Override
  public AuthResponseDTO login(AuthRequestDTO req) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(req.getUsuario(), req.getPassword()));
    } catch (AuthenticationException ex) {
      throw new BadCredentialsException("Credenciales inválidas");
    }

    Usuario u = usuarioRepository.findByUsuario(req.getUsuario())
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    
    if(Boolean.FALSE.equals(u.getActivo())) {
        throw new IllegalStateException("Usuario desactivado, comuníquese con el administrador");
    }

    UserDetails details = User.withUsername(u.getUsuario())
        .password(u.getPassword())
        .authorities(u.getRoles().stream().map(r -> "ROLE_" + r.getNombre()).toArray(String[]::new))
        .build();

    String token = jwtUtil.generateToken(details);
    Set<String> roles = u.getRoles().stream().map(Rol::getNombre).collect(Collectors.toSet());
    boolean isAdmin = u.getRoles().stream().anyMatch(Rol::isAdmin);

    // Construir menú a partir de los roles del usuario
    var menu = menuService.buildMenuForRoles(u.getRoles().stream().toList());

    return AuthResponseDTO.builder()
        .token(token)
        .usuario(u.getUsuario())
        .roles(roles)
        .admin(isAdmin)
        .menu(menu)
        .build();
  }
}
