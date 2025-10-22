// ==================== USUARIO SERVICE ====================

package com.predio.mijangos.modules.usuarios.service;

import com.predio.mijangos.modules.usuarios.dto.usuario.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {
    UsuarioResponseDTO findById(Integer id);
    UsuarioResponseDTO findByUsuario(String usuario);
    Page<UsuarioListDTO> findAll(Pageable pageable);
    UsuarioResponseDTO create(UsuarioCreateDTO dto);
    UsuarioResponseDTO update(Integer id, UsuarioUpdateDTO dto);
    void cambiarPassword(Integer id, CambioPasswordDTO dto);
    UsuarioResponseDTO cambiarEstado(Integer id, UsuarioEstadoDTO dto);
    UsuarioResponseDTO asignarRoles(Integer id, UsuarioRolesDTO dto);
    void delete(Integer id);
}
