package com.predio.mijangos.modules.security.controller;

import com.predio.mijangos.core.response.ApiResponse;
import com.predio.mijangos.modules.security.dto.*;
import com.predio.mijangos.modules.security.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/** Endpoints de administración de usuarios (sin borrado físico). */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Administración de usuarios del sistema")
public class UsuarioController {

  private final UsuarioService usuarioService;

  @Operation(summary = "Lista usuarios (paginado / filtros)")
  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<Page<UsuarioResponseDTO>>> list(
      @RequestParam(required = false) String q,
      @RequestParam(required = false) Boolean activo,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    var pageable = PageRequest.of(page, size, Sort.by("usuario").ascending());
    var body = usuarioService.list(q, activo, pageable);
    return ResponseEntity.ok(ApiResponse.ok("OK", body));
  }

  @Operation(summary = "Obtiene un usuario por id")
  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<UsuarioResponseDTO>> get(@PathVariable Integer id) {
    return ResponseEntity.ok(ApiResponse.ok("OK", usuarioService.getById(id)));
  }

  @Operation(summary = "Crea un nuevo usuario")
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<UsuarioResponseDTO>> create(@Valid @RequestBody UsuarioCreateRequestDTO req) {
    return ResponseEntity.ok(ApiResponse.ok("Creado", usuarioService.create(req)));
  }

  @Operation(summary = "Actualiza un usuario existente")
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<UsuarioResponseDTO>> update(
      @PathVariable Integer id, @Valid @RequestBody UsuarioUpdateRequestDTO req) {
    return ResponseEntity.ok(ApiResponse.ok("Actualizado", usuarioService.update(id, req)));
  }

  @Operation(summary = "Activa un usuario")
  @PatchMapping("/{id}/activar")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<Object>> activar(@PathVariable Integer id) {
    usuarioService.activar(id);
    return ResponseEntity.ok(ApiResponse.ok("Usuario activado", null));
  }

  @Operation(summary = "Desactiva un usuario")
  @PatchMapping("/{id}/desactivar")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<Object>> desactivar(@PathVariable Integer id) {
    usuarioService.desactivar(id);
    return ResponseEntity.ok(ApiResponse.ok("Usuario desactivado", null));
  }
}
