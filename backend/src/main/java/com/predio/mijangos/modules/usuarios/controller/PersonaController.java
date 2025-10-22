// ==================== PERSONA CONTROLLER ====================

package com.predio.mijangos.modules.usuarios.controller;

import com.predio.mijangos.core.response.ApiResponse;
import com.predio.mijangos.core.response.PageResponse;
import com.predio.mijangos.modules.usuarios.dto.persona.*;
import com.predio.mijangos.modules.usuarios.service.PersonaService;
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
@RequestMapping("/api/personas")
@RequiredArgsConstructor
@Tag(name = "Personas", description = "Gestión de personas (base para usuarios y clientes)")
@SecurityRequirement(name = "bearer-jwt")
public class PersonaController {

    private final PersonaService service;

    @GetMapping("/{id}")
    @Operation(summary = "Obtener persona por ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ApiResponse<PersonaResponseDTO>> findById(@PathVariable Integer id) {
        PersonaResponseDTO dto = service.findById(id);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @GetMapping("/identificacion/{identificacion}")
    @Operation(summary = "Buscar persona por identificación")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR', 'VENDEDOR')")
    public ResponseEntity<ApiResponse<PersonaResponseDTO>> findByIdentificacion(
            @PathVariable String identificacion) {
        PersonaResponseDTO dto = service.findByIdentificacion(identificacion);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @GetMapping
    @Operation(summary = "Listar personas con paginación")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    public ResponseEntity<ApiResponse<PageResponse<PersonaListDTO>>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "nombres") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("DESC")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PersonaListDTO> result = service.findAll(pageable);
        
        return ResponseEntity.ok(ApiResponse.ok(PageResponse.of(result)));
    }

    @PostMapping
    @Operation(summary = "Crear nueva persona")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PersonaResponseDTO>> create(
            @Valid @RequestBody PersonaCreateDTO dto) {
        PersonaResponseDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.created(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar persona")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PersonaResponseDTO>> update(
            @PathVariable Integer id,
            @Valid @RequestBody PersonaUpdateDTO dto) {
        PersonaResponseDTO updated = service.update(id, dto);
        return ResponseEntity.ok(ApiResponse.ok(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar persona (soft delete)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>ok("Persona eliminada exitosamente", null));
    }
}