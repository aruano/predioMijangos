package com.predio.mijangos.controller;

import com.predio.mijangos.dto.AuthRequestDTO;
import com.predio.mijangos.dto.AuthResponseDTO;
import com.predio.mijangos.dto.ApiResponse;
import com.predio.mijangos.service.AuthService;
import com.predio.mijangos.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@RequestBody AuthRequestDTO loginRequest) {
        AuthResponseDTO response = authService.login(loginRequest);
        return ResponseUtil.createResponse(
            "Token generado exitosamente",
            response,
            HttpStatus.OK
        );
    }
}
