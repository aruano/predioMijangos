package com.predio.mijangos.modules.usuarios.repository;

import com.predio.mijangos.modules.usuarios.domain.Pagina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaginaRepository extends JpaRepository<Pagina, Integer> {
    
    Optional<Pagina> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
    
    List<Pagina> findByModuloId(Integer idModulo);
    
    @Query("SELECT p FROM Pagina p WHERE p.modulo.id = :idModulo AND p.activo = true ORDER BY p.orden ASC")
    List<Pagina> findByModuloIdAndActivoTrueOrderByOrden(@Param("idModulo") Integer idModulo);
    
    @Query("SELECT p FROM Pagina p JOIN p.roles r WHERE r.id = :idRol ORDER BY p.modulo.orden, p.orden")
    List<Pagina> findByRolId(@Param("idRol") Integer idRol);
    
    @Query("SELECT DISTINCT p FROM Pagina p JOIN p.roles r JOIN r.usuarios u " +
           "WHERE u.id = :idUsuario AND p.activo = true " +
           "ORDER BY p.modulo.orden, p.orden")
    List<Pagina> findByUsuarioId(@Param("idUsuario") Integer idUsuario);
}