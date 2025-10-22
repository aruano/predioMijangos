// ==================== DEPARTAMENTO CONTROLLER ====================

package com.predio.mijangos.modules.geo.controller;

import com.predio.mijangos.core.response.ApiResponse;
import com.predio.mijangos.modules.geo.dto.DepartamentoResponseDTO;
import com.predio.mijangos.modules.geo.dto.SimpleItemDTO;
import com.predio.mijangos.modules.geo.service.DepartamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departamentos")
@RequiredArgsConstructor
@Tag(name = "Departamentos", description = "Gestión de departamentos de Guatemala")
@SecurityRequirement(name = "bearer-jwt")
public class DepartamentoController {

    private final DepartamentoService service;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener departamento por ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'VENDEDOR', 'BODEGUERO')")
    public ResponseEntity<ApiResponse<DepartamentoResponseDTO>> findById(@PathVariable Integer id) {
        DepartamentoResponseDTO dto = service.findById(id);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @GetMapping
    @Operation(summary = "Listar todos los departamentos")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'VENDEDOR', 'BODEGUERO')")
    public ResponseEntity<ApiResponse<List<DepartamentoResponseDTO>>> findAll() {
        List<DepartamentoResponseDTO> list = service.findAll();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/simple")
    @Operation(summary = "Listar departamentos (simplificado para combos)")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'VENDEDOR', 'BODEGUERO')")
    public ResponseEntity<ApiResponse<List<SimpleItemDTO>>> findAllSimple() {
        List<SimpleItemDTO> list = service.findAllSimple();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }
}