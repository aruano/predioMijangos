package com.predio.mijangos.modules.security.service;

import com.predio.mijangos.modules.security.dto.AuthRequestDTO;
import com.predio.mijangos.modules.security.dto.AuthResponseDTO;

public interface AuthService {
  AuthResponseDTO login(AuthRequestDTO req);
}
