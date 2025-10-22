// ==================== PÁGINA CONTROLLER ====================

package com.predio.mijangos.modules.usuarios.controller;

import com.predio.mijangos.core.response.ApiResponse;
import com.predio.mijangos.modules.usuarios.dto.pagina.*;
import com.predio.mijangos.modules.usuarios.service.PaginaService;
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
@RequestMapping("/api/paginas")
@RequiredArgsConstructor
@Tag(name = "Páginas", description = "Gestión de páginas y permisos")
@SecurityRequirement(name = "bearer-jwt")
public class PaginaController {

    private final PaginaService service;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener página por ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ApiResponse<PaginaResponseDTO>> findById(@PathVariable Integer id) {
        PaginaResponseDTO dto = service.findById(id);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @GetMapping
    @Operation(summary = "Listar todas las páginas")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<PaginaResponseDTO>>> findAll() {
        List<PaginaResponseDTO> list = service.findAll();
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/modulo/{idModulo}")
    @Operation(summary = "Listar páginas por módulo")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ApiResponse<List<PaginaResponseDTO>>> findByModulo(
            @PathVariable Integer idModulo) {
        List<PaginaResponseDTO> list = service.findByModulo(idModulo);
        return ResponseEntity.ok(ApiResponse.ok(list));
    }

    @GetMapping("/menu/usuario/{idUsuario}")
    @Operation(summary = "Obtener menú para un usuario")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR') or #idUsuario == authentication.principal.id")
    public ResponseEntity<ApiResponse<List<MenuModuloDTO>>> obtenerMenuPorUsuario(
            @PathVariable Integer idUsuario) {
        List<MenuModuloDTO> menu = service.obtenerMenuPorUsuario(idUsuario);
        return ResponseEntity.ok(ApiResponse.ok(menu));
    }

    @PostMapping
    @Operation(summary = "Crear nueva página")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PaginaResponseDTO>> create(
            @Valid @RequestBody PaginaCreateDTO dto) {
        PaginaResponseDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.created(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar página")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PaginaResponseDTO>> update(
            @PathVariable Integer id,
            @Valid @RequestBody PaginaUpdateDTO dto) {
        PaginaResponseDTO updated = service.update(id, dto);
        return ResponseEntity.ok(ApiResponse.ok(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar página")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>ok("Página eliminada exitosamente", null));
    }
}