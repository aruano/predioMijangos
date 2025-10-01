// com.predio.mijangos.modules.security.web.PaginaController
package com.predio.mijangos.modules.security.controller;

import com.predio.mijangos.core.response.ApiResponse;
import com.predio.mijangos.modules.security.dto.PageDTO;
import com.predio.mijangos.modules.security.domain.Pagina;
import com.predio.mijangos.modules.security.repo.PaginaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Catálogo de páginas para asignación a roles. */
@RestController
@RequestMapping("/api/pages")
@RequiredArgsConstructor
@Tag(name = "Páginas", description = "Catálogo de páginas (submódulos)")
public class PaginaController {

  private final PaginaRepository paginaRepo;

  @Operation(summary = "Lista todas las páginas (opcional: filtrar por moduloId o movil)")
  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<ApiResponse<List<PageDTO>>> list(
      @RequestParam(required = false) Integer moduloId,
      @RequestParam(required = false) Boolean movil
  ) {
    List<Pagina> paginas = paginaRepo.findAll(); // podrías agregar finders si quieres
    var body = paginas.stream()
        .filter(p -> moduloId == null || p.getModulo().getId().equals(moduloId))
        .filter(p -> movil == null || Boolean.TRUE.equals(p.getMovil()) == movil.booleanValue())
        .map(p -> new PageDTO(
            p.getId(), p.getNombre(), p.getMovil(), p.getIcon(),
            p.getRedirect(),
            p.getModulo().getId(), p.getModulo().getNombre()
        ))
        .toList();
    return ResponseEntity.ok(ApiResponse.ok("OK", body));
  }
}
