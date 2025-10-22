package com.predio.mijangos.modules.usuarios.service.impl;

import com.predio.mijangos.core.exception.BusinessException;
import com.predio.mijangos.core.exception.ResourceNotFoundException;
import com.predio.mijangos.modules.usuarios.domain.Pagina;
import com.predio.mijangos.modules.usuarios.domain.Rol;
import com.predio.mijangos.modules.usuarios.dto.rol.*;
import com.predio.mijangos.modules.usuarios.mapper.RolMapper;
import com.predio.mijangos.modules.usuarios.repository.PaginaRepository;
import com.predio.mijangos.modules.usuarios.repository.RolRepository;
import com.predio.mijangos.modules.usuarios.service.RolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RolServiceImpl implements RolService {

    private final RolRepository repository;
    private final PaginaRepository paginaRepository;
    private final RolMapper mapper;

    @Override
    public RolResponseDTO findById(Integer id) {
        log.debug("Buscando rol por ID: {}", id);
        Rol entity = repository.findByIdWithPaginas(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Rol no encontrado con ID: " + id
            ));
        return mapper.toResponseDTO(entity);
    }

    @Override
    public List<RolResponseDTO> findAll() {
        log.debug("Listando todos los roles");
        return mapper.toResponseDTOList(repository.findAll());
    }

    @Override
    public List<RolSimpleDTO> findAllActive() {
        log.debug("Listando roles activos");
        return mapper.toSimpleDTOList(repository.findAllActive());
    }

    @Override
    @Transactional
    public RolResponseDTO create(RolCreateDTO dto) {
        log.info("Creando nuevo rol: {}", dto.nombre());
        
        if (repository.existsByNombre(dto.nombre())) {
            throw new BusinessException(
                "Ya existe un rol con el nombre: " + dto.nombre()
            );
        }
        
        Rol entity = mapper.toEntity(dto);
        
        // Asignar páginas si se proporcionaron
        if (dto.paginasIds() != null && !dto.paginasIds().isEmpty()) {
            Set<Pagina> paginas = obtenerPaginasPorIds(dto.paginasIds());
            paginas.forEach(entity::addPagina);
        }
        
        Rol saved = repository.save(entity);
        log.info("Rol creado exitosamente con ID: {}", saved.getId());
        return mapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public RolResponseDTO update(Integer id, RolUpdateDTO dto) {
        log.info("Actualizando rol ID: {}", id);
        
        Rol entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Rol no encontrado con ID: " + id
            ));
        
        if (!entity.getNombre().equals(dto.nombre())
            && repository.existsByNombre(dto.nombre())) {
            throw new BusinessException(
                "Ya existe un rol con el nombre: " + dto.nombre()
            );
        }
        
        mapper.updateEntity(dto, entity);
        
        // Actualizar páginas si se proporcionaron
        if (dto.paginasIds() != null) {
            entity.getPaginas().clear();
            if (!dto.paginasIds().isEmpty()) {
                Set<Pagina> paginas = obtenerPaginasPorIds(dto.paginasIds());
                paginas.forEach(entity::addPagina);
            }
        }
        
        Rol updated = repository.save(entity);
        log.info("Rol actualizado exitosamente");
        return mapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public RolResponseDTO cambiarEstado(Integer id, RolEstadoDTO dto) {
        log.info("Cambiando estado del rol ID: {} a activo={}", id, dto.activo());
        
        Rol entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Rol no encontrado con ID: " + id
            ));
        
        entity.setActivo(dto.activo());
        Rol updated = repository.save(entity);
        
        log.info("Estado del rol actualizado exitosamente");
        return mapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public RolResponseDTO asignarPaginas(Integer id, RolPaginasDTO dto) {
        log.info("Asignando páginas al rol ID: {}", id);
        
        Rol entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Rol no encontrado con ID: " + id
            ));
        
        entity.getPaginas().clear();
        
        if (dto.paginasIds() != null && !dto.paginasIds().isEmpty()) {
            Set<Pagina> paginas = obtenerPaginasPorIds(dto.paginasIds());
            paginas.forEach(entity::addPagina);
        }
        
        Rol updated = repository.save(entity);
        log.info("Páginas asignadas exitosamente");
        return mapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Eliminando (soft delete) rol ID: {}", id);
        
        Rol entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Rol no encontrado con ID: " + id
            ));
        
        if (!entity.getUsuarios().isEmpty()) {
            throw new BusinessException(
                "No se puede eliminar el rol porque tiene usuarios asignados"
            );
        }
        
        repository.delete(entity);
        log.info("Rol eliminado exitosamente");
    }

    private Set<Pagina> obtenerPaginasPorIds(Set<Integer> paginasIds) {
        return paginasIds.stream()
            .map(idPagina -> paginaRepository.findById(idPagina)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Página no encontrada con ID: " + idPagina
                )))
            .collect(Collectors.toSet());
    }
}