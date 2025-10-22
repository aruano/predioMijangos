// ==================== ROL SERVICE ====================

package com.predio.mijangos.modules.usuarios.service;

import com.predio.mijangos.modules.usuarios.dto.rol.*;
import java.util.List;

public interface RolService {
    RolResponseDTO findById(Integer id);
    List<RolResponseDTO> findAll();
    List<RolSimpleDTO> findAllActive();
    RolResponseDTO create(RolCreateDTO dto);
    RolResponseDTO update(Integer id, RolUpdateDTO dto);
    RolResponseDTO cambiarEstado(Integer id, RolEstadoDTO dto);
    RolResponseDTO asignarPaginas(Integer id, RolPaginasDTO dto);
    void delete(Integer id);
}