package com.predio.mijangos.modules.security.repo;

import com.predio.mijangos.modules.security.domain.Pagina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Acceso a páginas; incluye consulta por conjunto de roles usando la tabla intermedia.
 */
public interface PaginaRepository extends JpaRepository<Pagina, Integer> {

  @Query("""
    SELECT p FROM Pagina p
    JOIN PaginaRol pr ON pr.pagina = p
    WHERE pr.rol.id IN :roleIds
  """)
  List<Pagina> findAllByRoleIds(List<Integer> roleIds);
}
