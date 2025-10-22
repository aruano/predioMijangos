// ==================== MÓDULO Y PÁGINA MAPPERS ====================

package com.predio.mijangos.modules.usuarios.mapper;

import com.predio.mijangos.modules.usuarios.domain.Modulo;
import com.predio.mijangos.modules.usuarios.dto.modulo.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ModuloMapper {

    @Mapping(target = "cantidadPaginas", expression = "java(modulo.getCantidadPaginas())")
    ModuloResponseDTO toResponseDTO(Modulo modulo);

    ModuloSimpleDTO toSimpleDTO(Modulo modulo);

    List<ModuloResponseDTO> toResponseDTOList(List<Modulo> modulos);
    List<ModuloSimpleDTO> toSimpleDTOList(List<Modulo> modulos);
}