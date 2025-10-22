// ==================== MÓDULO SERVICE ====================

package com.predio.mijangos.modules.usuarios.service;

import com.predio.mijangos.modules.usuarios.dto.modulo.*;
import java.util.List;

public interface ModuloService {
    ModuloResponseDTO findById(Integer id);
    List<ModuloResponseDTO> findAll();
    List<ModuloSimpleDTO> findAllActive();
}