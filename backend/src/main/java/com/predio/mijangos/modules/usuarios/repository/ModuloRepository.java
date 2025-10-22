package com.predio.mijangos.modules.usuarios.repository;

import com.predio.mijangos.modules.usuarios.domain.Modulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuloRepository extends JpaRepository<Modulo, Integer> {
    
    Optional<Modulo> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
    
    @Query("SELECT m FROM Modulo m WHERE m.activo = true ORDER BY m.orden ASC")
    List<Modulo> findAllActiveOrderByOrden();
}