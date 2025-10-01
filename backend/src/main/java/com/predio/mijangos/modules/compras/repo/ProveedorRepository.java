package com.predio.mijangos.modules.compras.repo;

import com.predio.mijangos.modules.compras.domain.Proveedor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio de acceso a datos para {@link Proveedor}. Incluye búsqueda simple
 * por texto y verificación de existencia por nombre.
 */
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {

    /**
     * Búsqueda de texto libre sobre campos clave. Nota: si la base está en
     * collation *_ci, no se requiere LOWER().
     */
    @Query("""
    SELECT p FROM Proveedor p
    WHERE (:q IS NULL OR :q = '' OR
           p.nombre LIKE CONCAT('%', :q, '%') OR
           p.correo LIKE CONCAT('%', :q, '%') OR
           p.telefono LIKE CONCAT('%', :q, '%') OR
           p.celular LIKE CONCAT('%', :q, '%'))
  """)
    Page<Proveedor> search(@Param("q") String q, Pageable pageable);

    /**
     * Valida si existe un proveedor con el mismo nombre (case-insensitive).
     */
    boolean existsByNombreIgnoreCase(String nombre);

}
