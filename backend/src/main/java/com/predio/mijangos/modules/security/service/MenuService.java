package com.predio.mijangos.modules.security.service;

import com.predio.mijangos.modules.security.domain.Rol;
import com.predio.mijangos.modules.security.dto.MenuDTO;
import java.util.List;

/**
 * Construye el menú autorizado (módulos/páginas) para un conjunto de roles.
 */
public interface MenuService {
  List<MenuDTO> buildMenuForRoles(List<Rol> roles);
}
