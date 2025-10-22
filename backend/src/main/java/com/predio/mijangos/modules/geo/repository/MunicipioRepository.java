// ==================== GEO REPOSITORIES ====================

package com.predio.mijangos.modules.geo.repository;

import com.predio.mijangos.modules.geo.domain.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio, Integer> {
    
    List<Municipio> findByDepartamentoId(Integer idDepartamento);
    boolean existsByNombreAndDepartamentoId(String nombre, Integer idDepartamento);
}
