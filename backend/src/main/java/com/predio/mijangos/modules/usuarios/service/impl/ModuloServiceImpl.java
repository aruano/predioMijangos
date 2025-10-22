package com.predio.mijangos.modules.usuarios.service.impl;

import com.predio.mijangos.core.exception.ResourceNotFoundException;
import com.predio.mijangos.modules.usuarios.domain.Modulo;
import com.predio.mijangos.modules.usuarios.dto.modulo.*;
import com.predio.mijangos.modules.usuarios.mapper.ModuloMapper;
import com.predio.mijangos.modules.usuarios.repository.ModuloRepository;
import com.predio.mijangos.modules.usuarios.service.ModuloService;
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
public class ModuloServiceImpl implements ModuloService {

    private final ModuloRepository repository;
    private final ModuloMapper mapper;

    @Override
    @Cacheable(value = "modulos", key = "#id")
    public ModuloResponseDTO findById(Integer id) {
        log.debug("Buscando módulo por ID: {}", id);
        Modulo entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Módulo no encontrado con ID: " + id
            ));
        return mapper.toResponseDTO(entity);
    }

    @Override
    @Cacheable("modulos")
    public List<ModuloResponseDTO> findAll() {
        log.debug("Listando todos los módulos");
        return mapper.toResponseDTOList(repository.findAll());
    }

    @Override
    @Cacheable("modulosActive")
    public List<ModuloSimpleDTO> findAllActive() {
        log.debug("Listando módulos activos");
        return mapper.toSimpleDTOList(repository.findAllActiveOrderByOrden());
    }
}
