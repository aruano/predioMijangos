package com.predio.mijangos.modules.security.service;

import com.predio.mijangos.modules.security.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RolService {
  Page<RolResponseDTO> list(String q, Pageable pageable);
  RolResponseDTO get(Integer id);
  RolResponseDTO create(RolCreateUpdateDTO dto);
  RolResponseDTO update(Integer id, RolCreateUpdateDTO dto);
  void delete(Integer id); // solo si no tiene usuarios asignados
}
