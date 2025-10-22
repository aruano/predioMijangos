// ==================== CLIENTE CONTROLLER ====================

package com.predio.mijangos.modules.usuarios.controller;

import com.predio.mijangos.core.response.ApiResponse;
import com.predio.mijangos.core.response.PageResponse;
import com.predio.mijangos.modules.usuarios.dto.cliente.*;
import com.predio.mijangos.modules.usuarios.service.ClienteService;
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
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Gestión de clientes")
@SecurityRequirement(name = "bearer-jwt")
public class ClienteController {

    private final ClienteService service;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'VENDEDOR')")
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> findById(@PathVariable Integer id) {
        ClienteResponseDTO dto = service.findById(id);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @GetMapping("/identificacion/{identificacion}")
    @Operation(summary = "Buscar cliente por identificación")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'VENDEDOR')")
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> findByIdentificacion(
            @PathVariable String identificacion) {
        ClienteResponseDTO dto = service.findByIdentificacion(identificacion);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @GetMapping
    @Operation(summary = "Listar clientes con paginación")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'VENDEDOR')")
    public ResponseEntity<ApiResponse<PageResponse<ClienteListDTO>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("DESC")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ClienteListDTO> result = service.findAll(pageable);
        
        return ResponseEntity.ok(ApiResponse.ok(PageResponse.of(result)));
    }

    @PostMapping
    @Operation(summary = "Crear nuevo cliente")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> create(
            @Valid @RequestBody ClienteCreateDTO dto) {
        ClienteResponseDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.created(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    public ResponseEntity<ApiResponse<ClienteResponseDTO>> update(
            @PathVariable Integer id,
            @Valid @RequestBody ClienteUpdateDTO dto) {
        ClienteResponseDTO updated = service.update(id, dto);
        return ResponseEntity.ok(ApiResponse.ok(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente (soft delete)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>ok("Cliente eliminado exitosamente", null));
    }
}