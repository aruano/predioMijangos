package com.predio.mijangos.modules.geo.service.impl;

import com.predio.mijangos.core.exception.ResourceNotFoundException;
import com.predio.mijangos.modules.geo.domain.Municipio;
import com.predio.mijangos.modules.geo.dto.MunicipioResponseDTO;
import com.predio.mijangos.modules.geo.dto.SimpleItemDTO;
import com.predio.mijangos.modules.geo.mapper.GeoMapper;
import com.predio.mijangos.modules.geo.repository.MunicipioRepository;
import com.predio.mijangos.modules.geo.service.MunicipioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MunicipioServiceImpl implements MunicipioService {

    private final MunicipioRepository repository;
    private final GeoMapper mapper;

    @Override
    @Cacheable(value = "municipios", key = "#id")
    public MunicipioResponseDTO findById(Integer id) {
        log.debug("Buscando municipio por ID: {}", id);
        Municipio entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Municipio no encontrado con ID: " + id
            ));
        return mapper.toResponseDTO(entity);
    }

    @Override
    @Cacheable("municipios")
    public List<MunicipioResponseDTO> findAll() {
        log.debug("Listando todos los municipios");
        return mapper.toMunicipioResponseDTOList(repository.findAll());
    }

    @Override
    @Cacheable(value = "municipiosByDepartamento", key = "#idDepartamento")
    public List<MunicipioResponseDTO> findByDepartamento(Integer idDepartamento) {
        log.debug("Listando municipios del departamento ID: {}", idDepartamento);
        return mapper.toMunicipioResponseDTOList(
            repository.findByDepartamentoId(idDepartamento)
        );
    }

    @Override
    @Cacheable(value = "municipiosByDepartamentoSimple", key = "#idDepartamento")
    public List<SimpleItemDTO> findByDepartamentoSimple(Integer idDepartamento) {
        log.debug("Listando municipios (simple) del departamento ID: {}", idDepartamento);
        return mapper.municipiosToSimpleItemDTOList(
            repository.findByDepartamentoId(idDepartamento)
        );
    }
}