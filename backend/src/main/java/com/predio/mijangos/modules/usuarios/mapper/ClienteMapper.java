// ==================== CLIENTE MAPPER ====================

package com.predio.mijangos.modules.usuarios.mapper;

import com.predio.mijangos.modules.usuarios.domain.Cliente;
import com.predio.mijangos.modules.usuarios.dto.cliente.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    /**
     * Convierte ClienteCreateDTO a entidad Cliente.
     * Los campos de auditoría se llenan automáticamente por Spring Data JPA.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "persona", ignore = true)
    Cliente toEntity(ClienteCreateDTO dto);

    @Mapping(target = "idPersona", source = "persona.id")
    @Mapping(target = "nombreCompleto", expression = "java(cliente.getNombreCompleto())")
    @Mapping(target = "tipoIdentificacion", source = "persona.tipoIdentificacion")
    @Mapping(target = "identificacion", source = "persona.identificacion")
    @Mapping(target = "correo", source = "persona.correo")
    @Mapping(target = "telefono", source = "persona.telefono")
    @Mapping(target = "direccion", source = "persona.direccionDomicilio")
    ClienteResponseDTO toResponseDTO(Cliente cliente);

    @Mapping(target = "nombreCompleto", expression = "java(cliente.getNombreCompleto())")
    @Mapping(target = "identificacion", source = "persona.identificacion")
    @Mapping(target = "correo", source = "persona.correo")
    @Mapping(target = "telefono", source = "persona.telefono")
    ClienteListDTO toListDTO(Cliente cliente);

    List<ClienteResponseDTO> toResponseDTOList(List<Cliente> clientes);
    List<ClienteListDTO> toListDTOList(List<Cliente> clientes);

    /**
     * Actualiza una entidad Cliente existente con datos del DTO.
     * Los campos de auditoría no se modifican.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "persona", ignore = true)
    void updateEntity(ClienteUpdateDTO dto, @MappingTarget Cliente cliente);
}