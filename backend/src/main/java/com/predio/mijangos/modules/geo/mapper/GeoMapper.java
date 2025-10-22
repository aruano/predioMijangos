package com.predio.mijangos.modules.geo.mapper;

import com.predio.mijangos.modules.geo.domain.Departamento;
import com.predio.mijangos.modules.geo.domain.Municipio;
import com.predio.mijangos.modules.geo.dto.DepartamentoResponseDTO;
import com.predio.mijangos.modules.geo.dto.MunicipioResponseDTO;
import com.predio.mijangos.modules.geo.dto.SimpleItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper para entidades geográficas.
 * Convierte entre entidades del dominio y DTOs de respuesta.
 * 
 * @author Predio Mijangos Dev Team
 * @version 1.0
 * @since 2025-10
 */
@Mapper(componentModel = "spring")
public interface GeoMapper {

    // ==================== DEPARTAMENTO ====================
    
    /**
     * Convierte Departamento a DepartamentoResponseDTO.
     * 
     * @param departamento Entidad Departamento
     * @return DTO de respuesta
     */
    @Mapping(target = "cantidadMunicipios", expression = "java(departamento.getMunicipios().size())")
    DepartamentoResponseDTO toResponseDTO(Departamento departamento);
    
    /**
     * Convierte lista de Departamentos a lista de DTOs.
     * 
     * @param departamentos Lista de entidades
     * @return Lista de DTOs
     */
    List<DepartamentoResponseDTO> toResponseDTOList(List<Departamento> departamentos);
    
    /**
     * Convierte Departamento a SimpleItemDTO para combos.
     * 
     * @param departamento Entidad Departamento
     * @return DTO simplificado
     */
    SimpleItemDTO toSimpleItemDTO(Departamento departamento);
    
    /**
     * Convierte lista de Departamentos a lista de SimpleItemDTO.
     * 
     * @param departamentos Lista de entidades
     * @return Lista de DTOs simplificados
     */
    List<SimpleItemDTO> toSimpleItemDTOList(List<Departamento> departamentos);
    
    // ==================== MUNICIPIO ====================
    
    /**
     * Convierte Municipio a MunicipioResponseDTO.
     * 
     * @param municipio Entidad Municipio
     * @return DTO de respuesta
     */
    @Mapping(target = "idDepartamento", source = "departamento.id")
    @Mapping(target = "nombreDepartamento", source = "departamento.nombre")
    MunicipioResponseDTO toResponseDTO(Municipio municipio);
    
    /**
     * Convierte lista de Municipios a lista de DTOs.
     * 
     * @param municipios Lista de entidades
     * @return Lista de DTOs
     */
    List<MunicipioResponseDTO> toMunicipioResponseDTOList(List<Municipio> municipios);
    
    /**
     * Convierte Municipio a SimpleItemDTO para combos.
     * 
     * @param municipio Entidad Municipio
     * @return DTO simplificado
     */
    SimpleItemDTO municipioToSimpleItemDTO(Municipio municipio);
    
    /**
     * Convierte lista de Municipios a lista de SimpleItemDTO.
     * 
     * @param municipios Lista de entidades
     * @return Lista de DTOs simplificados
     */
    List<SimpleItemDTO> municipiosToSimpleItemDTOList(List<Municipio> municipios);
}