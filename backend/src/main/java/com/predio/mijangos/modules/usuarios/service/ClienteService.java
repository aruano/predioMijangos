// ==================== CLIENTE SERVICE ====================

package com.predio.mijangos.modules.usuarios.service;

import com.predio.mijangos.modules.usuarios.dto.cliente.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {
    ClienteResponseDTO findById(Integer id);
    ClienteResponseDTO findByIdentificacion(String identificacion);
    Page<ClienteListDTO> findAll(Pageable pageable);
    ClienteResponseDTO create(ClienteCreateDTO dto);
    ClienteResponseDTO update(Integer id, ClienteUpdateDTO dto);
    void delete(Integer id);
}