package com.predio.mijangos.modules.geo.mapper;

import org.mapstruct.*;
import com.predio.mijangos.modules.geo.domain.Departamento;
import com.predio.mijangos.modules.geo.dto.*;

/**
 * Implementación por defecto de {@link DepartamentoService}. Aplica MapStruct para
 * mapping Entity↔DTO y coordina el repositorio.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepartamentoMapper {

    DepartamentoResponseDTO toListItem(Departamento entity);
}
