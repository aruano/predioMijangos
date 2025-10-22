package com.predio.mijangos.core.controller;

import com.predio.mijangos.core.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador raíz de la API.
 * 
 * <p>Proporciona información básica sobre la API y endpoints disponibles.
 * Útil para verificar que el servicio está activo y accesible.
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
@RestController
@RequestMapping("/")
@Tag(name = "API Root", description = "Información general de la API")
public class RootController {

    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * Endpoint raíz de la API.
     * 
     * <p>Proporciona información básica sobre el servicio y enlaces
     * a la documentación y recursos principales.
     * 
     * @return Información de bienvenida de la API
     */
    @GetMapping
    @Operation(
        summary = "Información de la API",
        description = "Obtiene información básica sobre el servicio y enlaces a recursos principales"
    )
    public ResponseEntity<ApiResponse<Map<String, Object>>> getApiInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("application", applicationName);
        info.put("version", "1.0.0");
        info.put("status", "running");
        info.put("timestamp", LocalDateTime.now());
        
        // Enlaces útiles
        Map<String, String> links = new HashMap<>();
        links.put("swagger", "/api/swagger-ui.html");
        links.put("api-docs", "/api/v3/api-docs");
        links.put("health", "/api/actuator/health");
        info.put("links", links);
        
        // Endpoints principales
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("auth", "/api/v1/auth");
        endpoints.put("usuarios", "/api/v1/usuarios");
        endpoints.put("clientes", "/api/v1/clientes");
        endpoints.put("roles", "/api/v1/roles");
        endpoints.put("personas", "/api/v1/personas");
        info.put("endpoints", endpoints);

        return ResponseEntity.ok(
            ApiResponse.ok("API Predio Mijangos activa y funcionando correctamente", info)
        );
    }

    /**
     * Endpoint de health check simple.
     * 
     * @return Estado del servicio
     */
    @GetMapping("/health")
    @Operation(
        summary = "Health check",
        description = "Verifica que el servicio está activo y respondiendo"
    )
    public ResponseEntity<ApiResponse<Map<String, String>>> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now().toString());
        
        return ResponseEntity.ok(
            ApiResponse.ok("Servicio activo", health)
        );
    }
}
