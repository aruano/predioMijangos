// ==================== MÓDULO CONTROLLER ====================

package com.predio.mijangos.modules.usuarios.controller;

import com.predio.mijangos.core.response.ApiResponse;
import com.predio.mijangos.modules.usuarios.dto.modulo.*;
import com.predio.mijangos.modules.usuarios.service.ModuloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modulos")
@RequiredArgsConstructor
@Tag(name = "Módulos", description = "Catálogo de módulos del sistema")
@SecurityRequirement(name = "bearer-jwt")
public class ModuloController {

    private final ModuloService service;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener módulo por ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ApiResponse<ModuloResponseDTO>> findById(@PathVariable Integer id) {
        ModuloResponseDTO dto = service.findById(id);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @GetMapping
    @Operation(summary = "Listar todos los módulos")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<ModuloResponseDTO>>> findAll() {
        List<ModuloResponseDTO> list = service.findAll();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar módulos activos (simplificado)")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<ModuloSimpleDTO>>> findAllActive() {
        List<ModuloSimpleDTO> list = service.findAllActive();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }
}