package com.predio.mijangos.modules.usuarios.mapper;

import com.predio.mijangos.modules.usuarios.domain.Pagina;
import com.predio.mijangos.modules.usuarios.dto.pagina.*;
import com.predio.mijangos.modules.usuarios.dto.rol.PaginaSimpleDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaginaMapper {

    /**
     * Convierte PaginaCreateDTO a entidad Pagina.
     * Los campos de auditoría no aplican en Pagina (no extiende BaseEntity).
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "modulo", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "activo", constant = "true")
    Pagina toEntity(PaginaCreateDTO dto);

    @Mapping(target = "idModulo", source = "modulo.id")
    @Mapping(target = "nombreModulo", source = "modulo.nombre")
    @Mapping(target = "cantidadRoles", expression = "java(pagina.getCantidadRoles())")
    PaginaResponseDTO toResponseDTO(Pagina pagina);

    @Mapping(target = "redirectWeb", source = "redirectWeb")
    @Mapping(target = "iconWeb", source = "iconWeb")
    PaginaSimpleDTO toSimpleDTO(Pagina pagina);

    List<PaginaResponseDTO> toResponseDTOList(List<Pagina> paginas);
    List<PaginaSimpleDTO> toSimpleDTOList(List<Pagina> paginas);

    /**
     * Actualiza una entidad Pagina existente con datos del DTO.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "modulo", ignore = true)
    @Mapping(target = "roles", ignore = true)
    void updateEntity(PaginaUpdateDTO dto, @MappingTarget Pagina pagina);
}