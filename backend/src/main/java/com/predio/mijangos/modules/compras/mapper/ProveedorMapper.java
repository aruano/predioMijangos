package com.predio.mijangos.modules.compras.mapper;

import org.mapstruct.*;
import com.predio.mijangos.modules.compras.domain.Proveedor;
import com.predio.mijangos.modules.compras.dto.*;

/**
 * Implementación por defecto de {@link ProveedorService}. Aplica MapStruct para
 * mapping Entity↔DTO y coordina el repositorio.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProveedorMapper {

    Proveedor toEntity(ProveedorCreateDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Proveedor target, ProveedorUpdateDTO dto);

    ProveedorResponseDTO toResponse(Proveedor entity);

    ProveedorListItemDTO toListItem(Proveedor entity);
}
