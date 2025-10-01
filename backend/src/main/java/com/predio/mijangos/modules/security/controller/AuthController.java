package com.predio.mijangos.modules.security.controller;

import com.predio.mijangos.modules.security.dto.AuthRequestDTO;
import com.predio.mijangos.modules.security.dto.AuthResponseDTO;
import com.predio.mijangos.core.response.ApiResponse;
import com.predio.mijangos.modules.security.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints de autenticación.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@Valid @RequestBody AuthRequestDTO req) {
    var result = authService.login(req);
    return ResponseEntity.ok(ApiResponse.ok("Exito",result));
  }
}
