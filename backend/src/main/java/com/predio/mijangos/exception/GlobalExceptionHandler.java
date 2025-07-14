package com.predio.mijangos.exception;

import com.predio.mijangos.dto.ApiResponse;
import com.predio.mijangos.util.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseUtil.createResponse("Usuario o contraseña inválidos", null, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUsernameNotFound(UsernameNotFoundException ex) {
        return ResponseUtil.createResponse("Usuario no encontrado", null, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllOtherExceptions(Exception ex, WebRequest request) {
        ex.printStackTrace(); // o usa un logger moderno
        return ResponseUtil.createResponse(
            ex.getMessage(),
            null,
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
