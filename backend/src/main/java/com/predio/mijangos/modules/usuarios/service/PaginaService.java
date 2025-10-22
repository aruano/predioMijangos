// ==================== PÁGINA SERVICE ====================

package com.predio.mijangos.modules.usuarios.service;

import com.predio.mijangos.modules.usuarios.dto.pagina.*;
import java.util.List;

public interface PaginaService {
    PaginaResponseDTO findById(Integer id);
    List<PaginaResponseDTO> findAll();
    List<PaginaResponseDTO> findByModulo(Integer idModulo);
    List<MenuModuloDTO> obtenerMenuPorUsuario(Integer idUsuario);
    PaginaResponseDTO create(PaginaCreateDTO dto);
    PaginaResponseDTO update(Integer id, PaginaUpdateDTO dto);
    void delete(Integer id);
}