package com.predio.mijangos.modules.security.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;
import java.util.Set;

/** Respuesta de login. */
@Getter
@Builder
public class AuthResponseDTO {
  private final String token;
  private final String usuario;
  private final Set<String> roles;
  private final boolean admin;
  private final List<MenuDTO> menu;
}
