package com.predio.mijangos.modules.security.repo;

import com.predio.mijangos.modules.security.domain.Usuario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.domain.*;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

  Optional<Usuario> findByUsuario(String usuario);

  boolean existsByUsuarioIgnoreCase(String usuario);

  @EntityGraph(attributePaths = {"roles","persona","persona.municipio","persona.municipio.departamento"})
  @Override
  Page<Usuario> findAll(Pageable pageable);

  @EntityGraph(attributePaths = {"roles","persona","persona.municipio","persona.municipio.departamento"})
  Page<Usuario> findByActivo(Boolean activo, Pageable pageable);

  @EntityGraph(attributePaths = {"roles","persona","persona.municipio","persona.municipio.departamento"})
  Page<Usuario> findByUsuarioContainingIgnoreCase(String q, Pageable pageable);
  
  Integer countByRoles_Id(Integer rolId); 
}
