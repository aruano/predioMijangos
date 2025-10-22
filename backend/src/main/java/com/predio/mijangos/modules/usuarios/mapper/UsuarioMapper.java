// ==================== USUARIO MAPPER ====================

package com.predio.mijangos.modules.usuarios.mapper;

import com.predio.mijangos.modules.usuarios.domain.Usuario;
import com.predio.mijangos.modules.usuarios.dto.usuario.*;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {RolMapper.class})
public interface UsuarioMapper {

    /**
     * Convierte UsuarioCreateDTO a entidad Usuario.
     * Los campos de auditoría se llenan automáticamente por Spring Data JPA.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "refreshTokens", ignore = true)
    @Mapping(target = "activo", constant = "true")
    Usuario toEntity(UsuarioCreateDTO dto);

    @Mapping(target = "idPersona", source = "persona.id")
    @Mapping(target = "nombreCompleto", expression = "java(usuario.getNombreCompleto())")
    @Mapping(target = "identificacion", source = "persona.identificacion")
    @Mapping(target = "correo", source = "persona.correo")
    @Mapping(target = "telefono", source = "persona.telefono")
    UsuarioResponseDTO toResponseDTO(Usuario usuario);

    @Mapping(target = "nombreCompleto", expression = "java(usuario.getNombreCompleto())")
    @Mapping(target = "correo", source = "persona.correo")
    @Mapping(target = "cantidadRoles", expression = "java(usuario.getRoles().size())")
    @Mapping(target = "nombresRoles", expression = "java(getNombresRoles(usuario))")
    UsuarioListDTO toListDTO(Usuario usuario);

    List<UsuarioResponseDTO> toResponseDTOList(List<Usuario> usuarios);
    List<UsuarioListDTO> toListDTOList(List<Usuario> usuarios);

    /**
     * Actualiza una entidad Usuario existente con datos del DTO.
     * Los campos de auditoría y relaciones no se modifican.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "persona", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "refreshTokens", ignore = true)
    void updateEntity(UsuarioUpdateDTO dto, @MappingTarget Usuario usuario);

    default String getNombresRoles(Usuario usuario) {
        return usuario.getRoles().stream()
            .map(rol -> rol.getNombre())
            .collect(Collectors.joining(", "));
    }
}
