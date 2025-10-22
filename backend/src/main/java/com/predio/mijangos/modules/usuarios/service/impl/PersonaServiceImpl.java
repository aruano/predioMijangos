package com.predio.mijangos.modules.usuarios.service.impl;

import com.predio.mijangos.core.exception.BusinessException;
import com.predio.mijangos.core.exception.ResourceNotFoundException;
import com.predio.mijangos.modules.geo.repository.MunicipioRepository;
import com.predio.mijangos.modules.usuarios.domain.Persona;
import com.predio.mijangos.modules.usuarios.dto.persona.*;
import com.predio.mijangos.modules.usuarios.mapper.PersonaMapper;
import com.predio.mijangos.modules.usuarios.repository.PersonaRepository;
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
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository repository;
    private final MunicipioRepository municipioRepository;
    private final PersonaMapper mapper;

    @Override
    public PersonaResponseDTO findById(Integer id) {
        log.debug("Buscando persona por ID: {}", id);
        Persona entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Persona no encontrada con ID: " + id
            ));
        return mapper.toResponseDTO(entity);
    }

    @Override
    public PersonaResponseDTO findByIdentificacion(String identificacion) {
        log.debug("Buscando persona por identificación: {}", identificacion);
        Persona entity = repository.findByIdentificacion(identificacion)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Persona no encontrada con identificación: " + identificacion
            ));
        return mapper.toResponseDTO(entity);
    }

    @Override
    public Page<PersonaListDTO> findAll(Pageable pageable) {
        log.debug("Listando personas - Página: {}", pageable.getPageNumber());
        return repository.findAll(pageable).map(mapper::toListDTO);
    }

    @Override
    @Transactional
    public PersonaResponseDTO create(PersonaCreateDTO dto) {
        log.info("Creando nueva persona: {}", dto.identificacion());
        
        // Validar identificación única
        if (repository.existsByIdentificacion(dto.identificacion())) {
            throw new BusinessException(
                "Ya existe una persona con la identificación: " + dto.identificacion()
            );
        }
        
        // Validar correo único si se proporciona
        if (dto.correo() != null && !dto.correo().isBlank() 
            && repository.existsByCorreo(dto.correo())) {
            throw new BusinessException(
                "Ya existe una persona con el correo: " + dto.correo()
            );
        }
        
        Persona entity = mapper.toEntity(dto);
        
        // Asignar municipio si se proporciona
        if (dto.idMunicipio() != null) {
            entity.setMunicipio(
                municipioRepository.findById(dto.idMunicipio())
                    .orElseThrow(() -> new ResourceNotFoundException(
                        "Municipio no encontrado con ID: " + dto.idMunicipio()
                    ))
            );
        }
        
        Persona saved = repository.save(entity);
        log.info("Persona creada exitosamente con ID: {}", saved.getId());
        return mapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public PersonaResponseDTO update(Integer id, PersonaUpdateDTO dto) {
        log.info("Actualizando persona ID: {}", id);
        
        Persona entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Persona no encontrada con ID: " + id
            ));
        
        // Validar identificación única si cambió
        if (!entity.getIdentificacion().equals(dto.identificacion())
            && repository.existsByIdentificacion(dto.identificacion())) {
            throw new BusinessException(
                "Ya existe una persona con la identificación: " + dto.identificacion()
            );
        }
        
        mapper.updateEntity(dto, entity);
        
        // Actualizar municipio si cambió
        if (dto.idMunicipio() != null) {
            entity.setMunicipio(
                municipioRepository.findById(dto.idMunicipio())
                    .orElseThrow(() -> new ResourceNotFoundException(
                        "Municipio no encontrado con ID: " + dto.idMunicipio()
                    ))
            );
        }
        
        Persona updated = repository.save(entity);
        log.info("Persona actualizada exitosamente");
        return mapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Eliminando (soft delete) persona ID: {}", id);
        
        Persona entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Persona no encontrada con ID: " + id
            ));
        
        repository.delete(entity); // Soft delete por @SQLDelete
        log.info("Persona eliminada exitosamente");
    }
}