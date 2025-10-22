// ==================== DEPARTAMENTO SERVICE ====================

package com.predio.mijangos.modules.geo.service;

import com.predio.mijangos.modules.geo.dto.DepartamentoResponseDTO;
import com.predio.mijangos.modules.geo.dto.SimpleItemDTO;

import java.util.List;

public interface DepartamentoService {
    DepartamentoResponseDTO findById(Integer id);
    List<DepartamentoResponseDTO> findAll();
    List<SimpleItemDTO> findAllSimple();
}
