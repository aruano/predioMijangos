package com.predio.mijangos.modules.security.service;

import com.predio.mijangos.modules.security.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {
  UsuarioResponseDTO create(UsuarioCreateRequestDTO req);
  UsuarioResponseDTO update(Integer id, UsuarioUpdateRequestDTO req);
  UsuarioResponseDTO getById(Integer id);
  Page<UsuarioResponseDTO> list(String q, Boolean activo, Pageable pageable);
  void activar(Integer id);
  void desactivar(Integer id);
}
