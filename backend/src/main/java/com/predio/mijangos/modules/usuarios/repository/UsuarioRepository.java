package com.predio.mijangos.modules.usuarios.repository;

import com.predio.mijangos.modules.usuarios.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer>, 
                                            JpaSpecificationExecutor<Usuario> {
    
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.roles WHERE u.usuario = :usuario")
    Optional<Usuario> findByUsuarioWithRoles(@Param("usuario") String usuario);
    
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.roles WHERE u.id = :id")
    Optional<Usuario> findByIdWithRoles(@Param("id") Integer id);
    
    Optional<Usuario> findByUsuario(String usuario);
    
    boolean existsByUsuario(String usuario);
    
    @Query("SELECT u FROM Usuario u WHERE u.persona.id = :idPersona")
    Optional<Usuario> findByPersonaId(@Param("idPersona") Integer idPersona);
    
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
           "FROM Usuario u WHERE u.activo = true AND u.deletedAt IS NULL")
    boolean hasActiveUsers();
}
