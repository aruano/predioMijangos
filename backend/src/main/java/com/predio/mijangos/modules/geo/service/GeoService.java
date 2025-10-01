package com.predio.mijangos.modules.geo.service;

import com.predio.mijangos.modules.geo.dto.*;
import java.util.List;

/**
 * Servicio de negocio para gestión de servicios geo. Orquesta validaciones y
 * persistencia sin exponer entidades al exterior.
 */
public interface GeoService {

    List<DepartamentoResponseDTO> listarDepartamentos();

    List<MunicipioResponseDTO> listarMunicipiosPorDepartamento(Integer departamentoId);
}
