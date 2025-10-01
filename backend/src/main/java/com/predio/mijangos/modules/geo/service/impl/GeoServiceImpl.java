package com.predio.mijangos.modules.geo.service.impl;

import com.predio.mijangos.modules.geo.repo.*;
import com.predio.mijangos.modules.geo.dto.*;
import com.predio.mijangos.modules.geo.mapper.*;
import com.predio.mijangos.modules.geo.service.GeoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de geocatálogos (SRP).
 */
@Service
@RequiredArgsConstructor
public class GeoServiceImpl implements GeoService {

    private final DepartamentoRepository departamentoRepo;
    private final MunicipioRepository municipioRepo;
    private final DepartamentoMapper departamentoMap;
    private final MunicipioMapper municipioMap;

    @Override
    @Transactional(readOnly = true)
    public java.util.List<DepartamentoResponseDTO> listarDepartamentos() {
        return departamentoRepo.findAll().stream().map(departamentoMap::toListItem).toList();
    }

    @Override
    public java.util.List<MunicipioResponseDTO> listarMunicipiosPorDepartamento(Integer deptoId) {
        return municipioRepo.findByDepartamento_Id(deptoId).stream()
                .map(municipioMap::toListItem).toList();
    }
}
