// ==================== ROL MAPPER ====================

package com.predio.mijangos.modules.usuarios.mapper;

import com.predio.mijangos.modules.usuarios.domain.Rol;
import com.predio.mijangos.modules.usuarios.dto.rol.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {PaginaMapper.class})
public interface RolMapper {

    /**
     * Convierte RolCreateDTO a entidad Rol.
     * Los campos de auditoría se llenan automáticamente por Spring Data JPA.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarios", ignore = true)
    @Mapping(target = "paginas", ignore = true)
    @Mapping(target = "activo", constant = "true")
    Rol toEntity(RolCreateDTO dto);

    @Mapping(target = "cantidadUsuarios", expression = "java(rol.getCantidadUsuarios())")
    @Mapping(target = "cantidadPaginas", expression = "java(rol.getCantidadPaginas())")
    @Mapping(target = "paginas", source = "paginas")
    RolResponseDTO toResponseDTO(Rol rol);

    RolSimpleDTO toSimpleDTO(Rol rol);

    List<RolResponseDTO> toResponseDTOList(List<Rol> roles);
    List<RolSimpleDTO> toSimpleDTOList(List<Rol> roles);

    /**
     * Actualiza una entidad Rol existente con datos del DTO.
     * Los campos de auditoría no se modifican.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "usuarios", ignore = true)
    @Mapping(target = "paginas", ignore = true)
    void updateEntity(RolUpdateDTO dto, @MappingTarget Rol rol);
}