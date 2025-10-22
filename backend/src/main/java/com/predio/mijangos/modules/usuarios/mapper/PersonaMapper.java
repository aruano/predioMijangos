// ==================== PERSONA MAPPER ====================

package com.predio.mijangos.modules.usuarios.mapper;

import com.predio.mijangos.modules.usuarios.domain.Persona;
import com.predio.mijangos.modules.usuarios.dto.persona.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonaMapper {

    /**
     * Convierte PersonaCreateDTO a entidad Persona.
     * Los campos de auditoría se llenan automáticamente por Spring Data JPA.
     */
    @Mapping(target = "id", ignore = true)
    Persona toEntity(PersonaCreateDTO dto);

    @Mapping(target = "nombreCompleto", expression = "java(persona.getNombreCompleto())")
    @Mapping(target = "nombreMunicipio", source = "municipio.nombre")
    @Mapping(target = "nombreDepartamento", source = "municipio.departamento.nombre")
    @Mapping(target = "idMunicipio", source = "municipio.id")
    PersonaResponseDTO toResponseDTO(Persona persona);

    @Mapping(target = "nombreCompleto", expression = "java(persona.getNombreCompleto())")
    @Mapping(target = "nombreMunicipio", source = "municipio.nombre")
    PersonaListDTO toListDTO(Persona persona);

    List<PersonaResponseDTO> toResponseDTOList(List<Persona> personas);
    List<PersonaListDTO> toListDTOList(List<Persona> personas);

    /**
     * Actualiza una entidad Persona existente con datos del DTO.
     * Los campos de auditoría no se modifican.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntity(PersonaUpdateDTO dto, @MappingTarget Persona persona);
}
