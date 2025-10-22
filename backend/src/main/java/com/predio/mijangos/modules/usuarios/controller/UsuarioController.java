// ==================== USUARIO CONTROLLER ====================

package com.predio.mijangos.modules.usuarios.controller;

import com.predio.mijangos.core.response.ApiResponse;
import com.predio.mijangos.core.response.PageResponse;
import com.predio.mijangos.modules.usuarios.dto.usuario.*;
import com.predio.mijangos.modules.usuarios.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
@SecurityRequirement(name = "bearer-jwt")
public class UsuarioController {

    private final UsuarioService service;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> findById(@PathVariable Integer id) {
        UsuarioResponseDTO dto = service.findById(id);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @GetMapping("/username/{usuario}")
    @Operation(summary = "Buscar usuario por código")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> findByUsuario(
            @PathVariable String usuario) {
        UsuarioResponseDTO dto = service.findByUsuario(usuario);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @GetMapping
    @Operation(summary = "Listar usuarios con paginación")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ApiResponse<PageResponse<UsuarioListDTO>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "usuario") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("DESC")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UsuarioListDTO> result = service.findAll(pageable);
        
        return ResponseEntity.ok(ApiResponse.ok(PageResponse.of(result)));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> create(
            @Valid @RequestBody UsuarioCreateDTO dto) {
        UsuarioResponseDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.created(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> update(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioUpdateDTO dto) {
        UsuarioResponseDTO updated = service.update(id, dto);
        return ResponseEntity.ok(ApiResponse.ok(updated));
    }

    @PatchMapping("/{id}/password")
    @Operation(summary = "Cambiar contraseña de usuario")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<ApiResponse<Void>> cambiarPassword(
            @PathVariable Integer id,
            @Valid @RequestBody CambioPasswordDTO dto) {
        service.cambiarPassword(id, dto);
        return ResponseEntity.ok(ApiResponse.<Void>ok("Contraseña actualizada exitosamente", null));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Activar o desactivar usuario")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> cambiarEstado(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioEstadoDTO dto) {
        UsuarioResponseDTO updated = service.cambiarEstado(id, dto);
        return ResponseEntity.ok(ApiResponse.ok("Estado actualizado exitosamente", updated));
    }

    @PatchMapping("/{id}/roles")
    @Operation(summary = "Asignar roles a usuario")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> asignarRoles(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioRolesDTO dto) {
        UsuarioResponseDTO updated = service.asignarRoles(id, dto);
        return ResponseEntity.ok(ApiResponse.ok("Roles asignados exitosamente", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario (soft delete)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>ok("Usuario eliminado exitosamente", null));
    }
}