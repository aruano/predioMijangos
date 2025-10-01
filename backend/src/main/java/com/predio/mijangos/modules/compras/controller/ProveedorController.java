package com.predio.mijangos.modules.compras.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.predio.mijangos.core.response.ApiResponse;
import com.predio.mijangos.modules.compras.dto.*;
import com.predio.mijangos.modules.compras.service.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;

/**
 * Endpoints REST para gestión de proveedores. Convenciones: - Entradas
 * validadas con Jakarta Validation. - Respuestas envueltas en ApiResponse<T>.
 */
@RestController
@RequestMapping("/api/providers")
@RequiredArgsConstructor
@Tag(name="Proveedores", description="CRUD de Proveedores")
public class ProveedorController {

    private final ProveedorService service;

    @Operation(summary="Listar proveedores activos")
    @GetMapping
    public ApiResponse<Page<ProveedorListItemDTO>> list(
            @RequestParam(required = false) String q,
            @PageableDefault(size = 20, sort = "nombre") Pageable pageable) {
        return ApiResponse.ok("Listado de proveedores", service.list(q, pageable));
    }

    @Operation(summary="Obtener proveedor por Id")
    @GetMapping("/{id}")
    public ApiResponse<ProveedorResponseDTO> get(@PathVariable Integer id) {
        return ApiResponse.ok("Detalle de proveedor", service.getById(id));
    }

    @Operation(summary="Crear proveedor")
    @PostMapping
    public ApiResponse<ProveedorResponseDTO> create(@Valid @RequestBody ProveedorCreateDTO dto) {
        return ApiResponse.ok("Proveedor creado", service.create(dto));
    }

    @Operation(summary="Actualizar proveedor")
    @PutMapping("/{id}")
    public ApiResponse<ProveedorResponseDTO> update(
            @PathVariable Integer id, @Valid @RequestBody ProveedorUpdateDTO dto) {
        // asegurar que el id de ruta prevalezca
        var fixed = new ProveedorUpdateDTO(
                id, dto.nombre(), dto.direccion(), dto.telefono(), dto.celular(),
                dto.correo(), dto.observaciones(), dto.activo()
        );
        return ApiResponse.ok("Proveedor actualizado", service.update(fixed));
    }

    @Operation(summary="Activar proveedor")
    @PatchMapping("/{id}/activar")
    public ApiResponse<Void> activar(@PathVariable Integer id) {
        service.toggleActivo(id, true);
        return ApiResponse.ok("Proveedor activado", null);
    }

    @Operation(summary="Desactivar proveedor")
    @PatchMapping("/{id}/desactivar")
    public ApiResponse<Void> desactivar(@PathVariable Integer id) {
        service.toggleActivo(id, false);
        return ApiResponse.ok("Proveedor desactivado", null);
    }
}
