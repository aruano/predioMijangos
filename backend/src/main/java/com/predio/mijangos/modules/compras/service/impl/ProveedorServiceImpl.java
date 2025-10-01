package com.predio.mijangos.modules.compras.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.predio.mijangos.modules.compras.repo.ProveedorRepository;
import com.predio.mijangos.modules.compras.mapper.ProveedorMapper;
import com.predio.mijangos.modules.compras.domain.Proveedor;
import com.predio.mijangos.modules.compras.dto.*;
import com.predio.mijangos.modules.compras.service.ProveedorService;

@Service
@RequiredArgsConstructor
@Transactional
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository repo;
    private final ProveedorMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ProveedorListItemDTO> list(String q, Pageable pageable) {
        if (q == null || q.isBlank()) {
            return repo.findAll(pageable).map(mapper::toListItem);
        }
        var page = repo
                .search(
                        q, pageable
                );
        return page.map(mapper::toListItem);
    }

    @Override
    public ProveedorResponseDTO create(ProveedorCreateDTO dto) {
        Proveedor entity = mapper.toEntity(dto);
        // valores por defecto
        if (entity.getActivo() == null) {
            entity.setActivo(true);
        }
        var saved = repo.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    public ProveedorResponseDTO update(ProveedorUpdateDTO dto) {
        Proveedor entity = repo.findById(dto.id())
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado: " + dto.id()));
        mapper.update(entity, dto);
        return mapper.toResponse(repo.save(entity));
    }

    @Override
    public void toggleActivo(Integer id, boolean activo) {
        Proveedor entity = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado: " + id));
        entity.setActivo(activo);
        repo.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public ProveedorResponseDTO getById(Integer id) {
        return repo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Proveedor no encontrado: " + id));
    }
}
