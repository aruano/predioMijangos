package com.predio.mijangos.modules.security.controller;

import com.predio.mijangos.core.response.ApiResponse;
import com.predio.mijangos.modules.security.dto.*;
import com.predio.mijangos.modules.security.service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/** Endpoints de administración de roles. */
@RestController
@RequestMapping("/api/rols")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Administración de roles del sistema")
public class RolController {

  private final RolService rolService;

  @Operation(summary = "Lista roles (paginado)")
  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<Page<RolResponseDTO>>> list(
      @RequestParam(required = false) String q,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    var pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());
    var body = rolService.list(q, pageable);
    return ResponseEntity.ok(ApiResponse.ok("OK", body));
  }

  @Operation(summary = "Obtiene un rol por id")
  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<RolResponseDTO>> get(@PathVariable Integer id) {
    return ResponseEntity.ok(ApiResponse.ok("OK", rolService.get(id)));
  }

  @Operation(summary = "Crea un rol")
  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<RolResponseDTO>> create(@Valid @RequestBody RolCreateUpdateDTO dto) {
    return ResponseEntity.ok(ApiResponse.ok("Creado", rolService.create(dto)));
  }

  @Operation(summary = "Actualiza un rol")
  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<RolResponseDTO>> update(
      @PathVariable Integer id, @Valid @RequestBody RolCreateUpdateDTO dto) {
    return ResponseEntity.ok(ApiResponse.ok("Actualizado", rolService.update(id, dto)));
  }

  @Operation(summary = "Elimina un rol (solo si no tiene usuarios asignados)")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<Object>> delete(@PathVariable Integer id) {
    rolService.delete(id);
    return ResponseEntity.ok(ApiResponse.ok("Eliminado", null));
  }
}
