package com.predio.mijangos.modules.geo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.predio.mijangos.core.response.ApiResponse;
import com.predio.mijangos.modules.geo.dto.*;
import com.predio.mijangos.modules.geo.service.GeoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;

/**
 * Endpoints REST para gestión de contenido geográfico. Convenciones: - Entradas
 * validadas con Jakarta Validation. - Respuestas envueltas en ApiResponse<T>.
 */
@RestController
@RequestMapping("/api/geo")
@RequiredArgsConstructor
@Tag(name = "Geo", description = "Catálogos de Departamento y Municipio")
public class GeoController {

    private final GeoService geoService;

    @Operation(summary = "Lista los departamentos")
    @GetMapping("/departamentos")
    public ApiResponse<List<DepartamentoResponseDTO>> departamentos() {
        return ApiResponse.ok("Listado de departamentos", geoService.listarDepartamentos());
    }

    @Operation(summary = "Lista municipios por departamento")
    @GetMapping("/municipios")
    public ApiResponse<List<MunicipioResponseDTO>> municipios(
            @RequestParam(required = true) Integer departamentoId
    ) {
        return ApiResponse.ok("Listado de municipios", geoService.listarMunicipiosPorDepartamento(departamentoId));
    }
}
