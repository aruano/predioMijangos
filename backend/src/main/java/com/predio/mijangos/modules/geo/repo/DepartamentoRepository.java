package com.predio.mijangos.modules.geo.repo;

import com.predio.mijangos.modules.geo.domain.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de acceso a datos para {@link Departamento}. Incluye búsqueda simple
 * por texto.
 */
public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {

}
