// com.predio.mijangos.modules.security.repo.RolRepository
package com.predio.mijangos.modules.security.repo;

import com.predio.mijangos.modules.security.domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolRepository extends JpaRepository<Rol, Integer> {
  boolean existsByNombreIgnoreCase(String nombre);
}
