// ==================== MUNICIPIO SERVICE ====================

package com.predio.mijangos.modules.geo.service;

import com.predio.mijangos.modules.geo.dto.MunicipioResponseDTO;
import com.predio.mijangos.modules.geo.dto.SimpleItemDTO;

import java.util.List;

public interface MunicipioService {
    MunicipioResponseDTO findById(Integer id);
    List<MunicipioResponseDTO> findAll();
    List<MunicipioResponseDTO> findByDepartamento(Integer idDepartamento);
    List<SimpleItemDTO> findByDepartamentoSimple(Integer idDepartamento);
}