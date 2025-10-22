// ==================== USUARIOS REPOSITORIES ====================

package com.predio.mijangos.modules.usuarios.repository;

import com.predio.mijangos.modules.usuarios.domain.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer>, 
                                           JpaSpecificationExecutor<Persona> {
    
    Optional<Persona> findByIdentificacion(String identificacion);
    boolean existsByIdentificacion(String identificacion);
    boolean existsByCorreo(String correo);
}