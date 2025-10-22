// ==================== MUNICIPIO CONTROLLER ====================

package com.predio.mijangos.modules.geo.controller;

import com.predio.mijangos.core.response.ApiResponse;
import com.predio.mijangos.modules.geo.dto.MunicipioResponseDTO;
import com.predio.mijangos.modules.geo.dto.SimpleItemDTO;
import com.predio.mijangos.modules.geo.service.MunicipioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/municipios")
@RequiredArgsConstructor
@Tag(name = "Municipios", description = "Gestión de municipios de Guatemala")
@SecurityRequirement(name = "bearer-jwt")
public class MunicipioController {

    private final MunicipioService service;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener municipio por ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'VENDEDOR', 'BODEGUERO')")
    public ResponseEntity<ApiResponse<MunicipioResponseDTO>> findById(@PathVariable Integer id) {
        MunicipioResponseDTO dto = service.findById(id);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @GetMapping
    @Operation(summary = "Listar todos los municipios")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'VENDEDOR', 'BODEGUERO')")
    public ResponseEntity<ApiResponse<List<MunicipioResponseDTO>>> findAll() {
        List<MunicipioResponseDTO> list = service.findAll();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/departamento/{idDepartamento}")
    @Operation(summary = "Listar municipios por departamento")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'VENDEDOR', 'BODEGUERO')")
    public ResponseEntity<ApiResponse<List<MunicipioResponseDTO>>> findByDepartamento(
            @PathVariable Integer idDepartamento) {
        List<MunicipioResponseDTO> list = service.findByDepartamento(idDepartamento);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/departamento/{idDepartamento}/simple")
    @Operation(summary = "Listar municipios por departamento (simplificado)")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'VENDEDOR', 'BODEGUERO')")
    public ResponseEntity<ApiResponse<List<SimpleItemDTO>>> findByDepartamentoSimple(
            @PathVariable Integer idDepartamento) {
        List<SimpleItemDTO> list = service.findByDepartamentoSimple(idDepartamento);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }
}