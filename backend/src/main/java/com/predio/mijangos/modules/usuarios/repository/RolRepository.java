package com.predio.mijangos.modules.usuarios.repository;

import com.predio.mijangos.modules.usuarios.domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    
    Optional<Rol> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
    
    @Query("SELECT r FROM Rol r LEFT JOIN FETCH r.paginas WHERE r.id = :id")
    Optional<Rol> findByIdWithPaginas(@Param("id") Integer id);
    
    @Query("SELECT r FROM Rol r WHERE r.activo = true AND r.deletedAt IS NULL")
    List<Rol> findAllActive();
    
    @Query("SELECT r FROM Rol r JOIN r.usuarios u WHERE u.id = :idUsuario")
    List<Rol> findByUsuarioId(@Param("idUsuario") Integer idUsuario);
}