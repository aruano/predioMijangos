package com.predio.mijangos.modules.geo.repo;

import com.predio.mijangos.modules.geo.domain.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repositorio de acceso a datos para {@link Municipio}. Incluye búsqueda simple
 * por texto y verificación de existencia por nombre.
 */
public interface MunicipioRepository extends JpaRepository<Municipio, Integer> {

    List<Municipio> findByDepartamento_Id(Integer departamentoId);
}
