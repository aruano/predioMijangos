package com.predio.mijangos.modules.security.repo;

import com.predio.mijangos.modules.security.domain.PaginaRol;
import com.predio.mijangos.modules.security.domain.PaginaRolId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PaginaRolRepository extends JpaRepository<PaginaRol, PaginaRolId> {
  @Transactional
  void deleteByRol_Id(Integer rolId);
}
