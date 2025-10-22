package com.predio.mijangos.modules.usuarios.service.impl;

import com.predio.mijangos.core.exception.BusinessException;
import com.predio.mijangos.core.exception.ResourceNotFoundException;
import com.predio.mijangos.modules.usuarios.domain.Cliente;
import com.predio.mijangos.modules.usuarios.domain.Persona;
import com.predio.mijangos.modules.usuarios.dto.cliente.*;
import com.predio.mijangos.modules.usuarios.mapper.ClienteMapper;
import com.predio.mijangos.modules.usuarios.repository.ClienteRepository;
import com.predio.mijangos.modules.usuarios.service.ClienteService;
import com.predio.mijangos.modules.usuarios.service.PersonaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;
    private final PersonaService personaService;
    private final ClienteMapper mapper;

    @Override
    public ClienteResponseDTO findById(Integer id) {
        log.debug("Buscando cliente por ID: {}", id);
        Cliente entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Cliente no encontrado con ID: " + id
            ));
        return mapper.toResponseDTO(entity);
    }

    @Override
    public ClienteResponseDTO findByIdentificacion(String identificacion) {
        log.debug("Buscando cliente por identificación: {}", identificacion);
        Cliente entity = repository.findByIdentificacion(identificacion)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Cliente no encontrado con identificación: " + identificacion
            ));
        return mapper.toResponseDTO(entity);
    }

    @Override
    public Page<ClienteListDTO> findAll(Pageable pageable) {
        log.debug("Listando clientes - Página: {}", pageable.getPageNumber());
        return repository.findAll(pageable).map(mapper::toListDTO);
    }

    @Override
    @Transactional
    public ClienteResponseDTO create(ClienteCreateDTO dto) {
        log.info("Creando nuevo cliente");
        
        // Crear persona primero
        var personaDTO = personaService.create(dto.persona());
        
        // Crear cliente
        Cliente entity = mapper.toEntity(dto);
        Persona persona = new Persona();
        persona.setId(personaDTO.id());
        entity.setPersona(persona);
        
        Cliente saved = repository.save(entity);
        log.info("Cliente creado exitosamente con ID: {}", saved.getId());
        return mapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public ClienteResponseDTO update(Integer id, ClienteUpdateDTO dto) {
        log.info("Actualizando cliente ID: {}", id);
        
        Cliente entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Cliente no encontrado con ID: " + id
            ));
        
        mapper.updateEntity(dto, entity);
        
        Cliente updated = repository.save(entity);
        log.info("Cliente actualizado exitosamente");
        return mapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Eliminando (soft delete) cliente ID: {}", id);
        
        Cliente entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Cliente no encontrado con ID: " + id
            ));
        
        repository.delete(entity);
        log.info("Cliente eliminado exitosamente");
    }
}