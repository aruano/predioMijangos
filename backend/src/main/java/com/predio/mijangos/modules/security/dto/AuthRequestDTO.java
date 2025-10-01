package com.predio.mijangos.modules.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/** Petición de login. */
@Getter @Setter
public class AuthRequestDTO {
  @NotBlank private String usuario;
  @NotBlank private String password;
}
