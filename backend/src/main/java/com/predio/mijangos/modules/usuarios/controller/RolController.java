// ==================== ROL CONTROLLER ====================

package com.predio.mijangos.modules.usuarios.controller;

import com.predio.mijangos.core.response.ApiResponse;
import com.predio.mijangos.modules.usuarios.dto.rol.*;
import com.predio.mijangos.modules.usuarios.service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Gestión de roles y permisos")
@SecurityRequirement(name = "bearer-jwt")
public class RolController {

    private final RolService service;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener rol por ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ApiResponse<RolResponseDTO>> findById(@PathVariable Integer id) {
        RolResponseDTO dto = service.findById(id);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @GetMapping
    @Operation(summary = "Listar todos los roles")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<RolResponseDTO>>> findAll() {
        List<RolResponseDTO> list = service.findAll();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar roles activos (simplificado)")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<RolSimpleDTO>>> findAllActive() {
        List<RolSimpleDTO> list = service.findAllActive();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo rol")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RolResponseDTO>> create(
            @Valid @RequestBody RolCreateDTO dto) {
        RolResponseDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.created(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar rol")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RolResponseDTO>> update(
            @PathVariable Integer id,
            @Valid @RequestBody RolUpdateDTO dto) {
        RolResponseDTO updated = service.update(id, dto);
        return ResponseEntity.ok(ApiResponse.ok(updated));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Activar o desactivar rol")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RolResponseDTO>> cambiarEstado(
            @PathVariable Integer id,
            @Valid @RequestBody RolEstadoDTO dto) {
        RolResponseDTO updated = service.cambiarEstado(id, dto);
        return ResponseEntity.ok(ApiResponse.ok("Estado actualizado exitosamente", updated));
    }

    @PatchMapping("/{id}/paginas")
    @Operation(summary = "Asignar páginas a rol")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RolResponseDTO>> asignarPaginas(
            @PathVariable Integer id,
            @Valid @RequestBody RolPaginasDTO dto) {
        RolResponseDTO updated = service.asignarPaginas(id, dto);
        return ResponseEntity.ok(ApiResponse.ok("Páginas asignadas exitosamente", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar rol (soft delete)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>ok("Rol eliminado exitosamente", null));
    }
}