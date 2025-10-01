package com.predio.mijangos.modules.security.service.impl;

import com.predio.mijangos.modules.security.domain.Pagina;
import com.predio.mijangos.modules.security.domain.Rol;
import com.predio.mijangos.modules.security.dto.MenuDTO;
import com.predio.mijangos.modules.security.dto.PageDTO;
import com.predio.mijangos.modules.security.repo.PaginaRepository;
import com.predio.mijangos.modules.security.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementación: obtiene páginas por roles y las agrupa por módulo.
 * SRP: lógica de armado del menú; no mezcla persistencia ni seguridad HTTP.
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

  private final PaginaRepository paginaRepository;

  @Override
  public List<MenuDTO> buildMenuForRoles(List<Rol> roles) {
    if (roles == null || roles.isEmpty()) return List.of();

    var roleIds = roles.stream().map(Rol::getId).toList();
    List<Pagina> paginas = paginaRepository.findAllByRoleIds(roleIds);

    // Agrupar pages por módulo
    Map<Integer, List<Pagina>> byModulo = paginas.stream()
        .collect(Collectors.groupingBy(p -> p.getModulo().getId()));

    // Mapear a DTOs
    List<MenuDTO> menu = new ArrayList<>();
    for (var entry : byModulo.entrySet()) {
      var moduloId = entry.getKey();
      var anyPagina = entry.getValue().get(0);
      var moduloNombre = anyPagina.getModulo().getNombre();

      var paginasDTO = entry.getValue().stream()
          .sorted(Comparator.comparing(Pagina::getNombre))
          .map(p -> new PageDTO(
              p.getId(),
              p.getNombre(),
              Boolean.TRUE.equals(p.getMovil()),
              p.getIcon(),
              p.getRedirect(),
              p.getModulo().getId(),
              p.getModulo().getNombre()
          ))
          .toList();

      menu.add(new MenuDTO(moduloId, moduloNombre, paginasDTO));
    }

    // Ordenar módulos por id
    menu.sort(Comparator.comparing(MenuDTO::id));
    return menu;
  }
}
