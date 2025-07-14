package com.predio.mijangos.service;

import com.predio.mijangos.dto.AuthRequestDTO;
import com.predio.mijangos.dto.AuthResponseDTO;
import com.predio.mijangos.model.Usuario;
import com.predio.mijangos.repository.UsuarioRepository;
import com.predio.mijangos.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private JwtUtil jwtUtil;

    public AuthResponseDTO login(AuthRequestDTO loginRequest) {
        // Autenticación con Spring Security
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsuario(), loginRequest.getPassword())
        );

        // Busca usuario y genera el JWT
        Usuario usuario = usuarioRepository.findByUsuario(loginRequest.getUsuario())
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        UserDetails userDetails = org.springframework.security.core.userdetails.User
            .withUsername(usuario.getUsuario())
            .password(usuario.getPassword())
            .authorities(usuario.getRoles().stream().map(r -> r.getNombre()).toArray(String[]::new))
            .build();

        String token = jwtUtil.generateToken(userDetails);

        return AuthResponseDTO.builder()
                .token(token)
                .usuario(usuario.getUsuario())
                .roles(usuario.getRoles())
                .build();
    }
}
