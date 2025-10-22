package com.predio.mijangos.modules.usuarios.service.impl;

import com.predio.mijangos.core.exception.BusinessException;
import com.predio.mijangos.core.exception.ResourceNotFoundException;
import com.predio.mijangos.modules.usuarios.domain.Modulo;
import com.predio.mijangos.modules.usuarios.domain.Pagina;
import com.predio.mijangos.modules.usuarios.dto.pagina.*;
import com.predio.mijangos.modules.usuarios.mapper.PaginaMapper;
import com.predio.mijangos.modules.usuarios.repository.ModuloRepository;
import com.predio.mijangos.modules.usuarios.repository.PaginaRepository;
import com.predio.mijangos.modules.usuarios.service.PaginaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PaginaServiceImpl implements PaginaService {

    private final PaginaRepository repository;
    private final ModuloRepository moduloRepository;
    private final PaginaMapper mapper;

    @Override
    public PaginaResponseDTO findById(Integer id) {
        log.debug("Buscando página por ID: {}", id);
        Pagina entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Página no encontrada con ID: " + id
            ));
        return mapper.toResponseDTO(entity);
    }

    @Override
    public List<PaginaResponseDTO> findAll() {
        log.debug("Listando todas las páginas");
        return mapper.toResponseDTOList(repository.findAll());
    }

    @Override
    public List<PaginaResponseDTO> findByModulo(Integer idModulo) {
        log.debug("Listando páginas del módulo ID: {}", idModulo);
        return mapper.toResponseDTOList(repository.findByModuloId(idModulo));
    }

    @Override
    public List<MenuModuloDTO> obtenerMenuPorUsuario(Integer idUsuario) {
        log.debug("Obteniendo menú para usuario ID: {}", idUsuario);
        
        List<Pagina> paginas = repository.findByUsuarioId(idUsuario);
        
        // Agrupar por módulo
        Map<Modulo, List<Pagina>> paginasPorModulo = paginas.stream()
            .collect(Collectors.groupingBy(Pagina::getModulo));
        
        // Construir DTOs de menú
        return paginasPorModulo.entrySet().stream()
            .map(entry -> {
                Modulo modulo = entry.getKey();
                List<MenuPaginaDTO> menuPaginas = entry.getValue().stream()
                    .map(p -> MenuPaginaDTO.builder()
                        .id(p.getId())
                        .nombre(p.getNombre())
                        .icono(p.getIconWeb())
                        .ruta(p.getRedirectWeb())
                        .movil(p.getMovil())
                        .orden(p.getOrden())
                        .build())
                    .collect(Collectors.toList());
                
                return MenuModuloDTO.builder()
                    .id(modulo.getId())
                    .nombre(modulo.getNombre())
                    .orden(modulo.getOrden())
                    .paginas(menuPaginas)
                    .build();
            })
            .sorted((m1, m2) -> m1.orden().compareTo(m2.orden()))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaginaResponseDTO create(PaginaCreateDTO dto) {
        log.info("Creando nueva página: {}", dto.nombre());
        
        if (repository.existsByNombre(dto.nombre())) {
            throw new BusinessException(
                "Ya existe una página con el nombre: " + dto.nombre()
            );
        }
        
        Pagina entity = mapper.toEntity(dto);
        
        Modulo modulo = moduloRepository.findById(dto.idModulo())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Módulo no encontrado con ID: " + dto.idModulo()
            ));
        entity.setModulo(modulo);
        
        Pagina saved = repository.save(entity);
        log.info("Página creada exitosamente con ID: {}", saved.getId());
        return mapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public PaginaResponseDTO update(Integer id, PaginaUpdateDTO dto) {
        log.info("Actualizando página ID: {}", id);
        
        Pagina entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Página no encontrada con ID: " + id
            ));
        
        if (!entity.getNombre().equals(dto.nombre())
            && repository.existsByNombre(dto.nombre())) {
            throw new BusinessException(
                "Ya existe una página con el nombre: " + dto.nombre()
            );
        }
        
        mapper.updateEntity(dto, entity);
        
        Modulo modulo = moduloRepository.findById(dto.idModulo())
            .orElseThrow(() -> new ResourceNotFoundException(
                "Módulo no encontrado con ID: " + dto.idModulo()
            ));
        entity.setModulo(modulo);
        
        Pagina updated = repository.save(entity);
        log.info("Página actualizada exitosamente");
        return mapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Eliminando página ID: {}", id);
        
        Pagina entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Página no encontrada con ID: " + id
            ));
        
        repository.delete(entity);
        log.info("Página eliminada exitosamente");
    }
}