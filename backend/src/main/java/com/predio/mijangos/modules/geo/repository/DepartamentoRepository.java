// ==================== GEO REPOSITORIES ====================

package com.predio.mijangos.modules.geo.repository;

import com.predio.mijangos.modules.geo.domain.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {
    
    Optional<Departamento> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}