package com.predio.mijangos.modules.usuarios.repository;

import com.predio.mijangos.modules.usuarios.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>, 
                                            JpaSpecificationExecutor<Cliente> {
    
    @Query("SELECT c FROM Cliente c WHERE c.persona.id = :idPersona")
    Optional<Cliente> findByPersonaId(@Param("idPersona") Integer idPersona);
    
    @Query("SELECT c FROM Cliente c WHERE c.persona.identificacion = :identificacion")
    Optional<Cliente> findByIdentificacion(@Param("identificacion") String identificacion);
}