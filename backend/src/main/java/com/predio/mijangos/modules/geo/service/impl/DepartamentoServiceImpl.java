package com.predio.mijangos.modules.geo.service.impl;

import com.predio.mijangos.core.exception.ResourceNotFoundException;
import com.predio.mijangos.modules.geo.domain.Departamento;
import com.predio.mijangos.modules.geo.dto.DepartamentoResponseDTO;
import com.predio.mijangos.modules.geo.dto.SimpleItemDTO;
import com.predio.mijangos.modules.geo.mapper.GeoMapper;
import com.predio.mijangos.modules.geo.repository.DepartamentoRepository;
import com.predio.mijangos.modules.geo.service.DepartamentoService;
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
public class DepartamentoServiceImpl implements DepartamentoService {

    private final DepartamentoRepository repository;
    private final GeoMapper mapper;

    @Override
    @Cacheable(value = "departamentos", key = "#id")
    public DepartamentoResponseDTO findById(Integer id) {
        log.debug("Buscando departamento por ID: {}", id);
        Departamento entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Departamento no encontrado con ID: " + id
            ));
        return mapper.toResponseDTO(entity);
    }

    @Override
    @Cacheable("departamentos")
    public List<DepartamentoResponseDTO> findAll() {
        log.debug("Listando todos los departamentos");
        return mapper.toResponseDTOList(repository.findAll());
    }

    @Override
    @Cacheable("departamentosSimple")
    public List<SimpleItemDTO> findAllSimple() {
        log.debug("Listando departamentos (simple)");
        return mapper.toSimpleItemDTOList(repository.findAll());
    }
}