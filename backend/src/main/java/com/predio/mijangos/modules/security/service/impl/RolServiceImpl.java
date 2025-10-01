// com.predio.mijangos.modules.security.service.RolServiceImpl
package com.predio.mijangos.modules.security.service.impl;

import com.predio.mijangos.modules.security.domain.*;
import com.predio.mijangos.modules.security.dto.*;
import com.predio.mijangos.modules.security.repo.*;
import com.predio.mijangos.modules.security.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RolServiceImpl implements RolService {

  private final RolRepository rolRepo;
  private final UsuarioRepository usuarioRepo;
  private final PaginaRepository paginaRepo;
  private final PaginaRolRepository paginaRolRepo;

  @Override @Transactional(readOnly = true)
  public Page<RolResponseDTO> list(String q, Pageable pageable) {
    Page<Rol> page = (q == null || q.isBlank())
        ? rolRepo.findAll(pageable)
        : rolRepo.findAll(pageable); 
    return page.map(this::toDtoWithPages); 
  }

  @Override @Transactional(readOnly = true)
  public RolResponseDTO get(Integer id) {
    Rol r = rolRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Rol no encontrado"));
    return toDtoWithPages(r);
  }

  @Override @Transactional
  public RolResponseDTO create(RolCreateUpdateDTO dto) {
    if (rolRepo.existsByNombreIgnoreCase(dto.nombre())) {
      throw new IllegalArgumentException("Ya existe un rol con ese nombre");
    }
    Rol r = Rol.builder()
        .nombre(dto.nombre().trim())
        .descripcion(dto.descripcion())
        .admin(Boolean.TRUE.equals(dto.admin()))
        .build();
    r = rolRepo.save(r);

    assignPages(r, dto.paginaIds());   // ← asignación inicial
    return toDtoWithPages(r);
  }

  @Override @Transactional
  public RolResponseDTO update(Integer id, RolCreateUpdateDTO dto) {
    Rol r = rolRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Rol no encontrado"));

    if (!r.getNombre().equalsIgnoreCase(dto.nombre()) && rolRepo.existsByNombreIgnoreCase(dto.nombre())) {
      throw new IllegalArgumentException("Ya existe un rol con ese nombre");
    }

    r.setNombre(dto.nombre().trim());
    r.setDescripcion(dto.descripcion());
    r.setAdmin(Boolean.TRUE.equals(dto.admin()));
    r = rolRepo.save(r);

    // Reasignar páginas (limpiar y volver a crear)
    assignPages(r, dto.paginaIds());
    return toDtoWithPages(r);
  }

  @Override @Transactional
  public void delete(Integer id) {
    Rol r = rolRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Rol no encontrado"));
    Integer vinculados = usuarioRepo.countByRoles_Id(id);
    if (vinculados > 0) {
      throw new IllegalStateException("No se puede eliminar el rol: hay usuarios asociados");
    }
    // limpiar páginas asociadas (por integridad de la intermedia)
    paginaRolRepo.deleteByRol_Id(id);
    rolRepo.delete(r);
  }

  // ---------- Helpers ----------

  /** Asigna páginas a un rol: borra las actuales y crea nuevas (idempotente). */
  private void assignPages(Rol rol, List<Integer> paginaIds) {
    paginaRolRepo.deleteByRol_Id(rol.getId()); // limpia actuales

    if (paginaIds == null || paginaIds.isEmpty()) return;

    // Cargar páginas existentes y validar
    List<Pagina> paginas = paginaRepo.findAllById(paginaIds);
    if (paginas.size() != new HashSet<>(paginaIds).size()) {
      throw new NoSuchElementException("Alguna página no existe");
    }

    // Crear filas en TBL_Pagina_Rol
    List<PaginaRol> asignaciones = new ArrayList<>();
    for (Pagina p : paginas) {
      PaginaRol pr = PaginaRol.builder()
          .id(new PaginaRolId(p.getId(), rol.getId().intValue()))
          .pagina(p)
          .rol(rol)
          .build();
      asignaciones.add(pr);
    }
    paginaRolRepo.saveAll(asignaciones);
  }

  /** Mapea Rol + páginas (usando PaginaRepository vía roles) a DTO de respuesta. */
  private RolResponseDTO toDtoWithPages(Rol r) {
    // Traer páginas de este rol (reusar query por roleIds)
    List<Pagina> paginas = paginaRepo.findAllByRoleIds(List.of(r.getId().intValue()));
    var pagesDto = paginas.stream().map(p ->
        new PageDTO(
            p.getId(),
            p.getNombre(),
            Boolean.TRUE.equals(p.getMovil()),
            p.getIcon(),
            p.getRedirect(),
            p.getModulo().getId(),
            p.getModulo().getNombre()
        )
    ).toList();

    return new RolResponseDTO(r.getId(), r.getNombre(), r.getDescripcion(), r.isAdmin(), pagesDto);
  }
}
