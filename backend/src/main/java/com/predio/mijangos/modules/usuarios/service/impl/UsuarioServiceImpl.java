package com.predio.mijangos.modules.usuarios.service.impl;

import com.predio.mijangos.core.exception.BusinessException;
import com.predio.mijangos.core.exception.ResourceNotFoundException;
import com.predio.mijangos.modules.usuarios.domain.Persona;
import com.predio.mijangos.modules.usuarios.domain.Rol;
import com.predio.mijangos.modules.usuarios.domain.Usuario;
import com.predio.mijangos.modules.usuarios.dto.usuario.*;
import com.predio.mijangos.modules.usuarios.mapper.UsuarioMapper;
import com.predio.mijangos.modules.usuarios.repository.RolRepository;
import com.predio.mijangos.modules.usuarios.repository.UsuarioRepository;
import com.predio.mijangos.modules.usuarios.service.PersonaService;
import com.predio.mijangos.modules.usuarios.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repository;
    private final RolRepository rolRepository;
    private final PersonaService personaService;
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioResponseDTO findById(Integer id) {
        log.debug("Buscando usuario por ID: {}", id);
        Usuario entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Usuario no encontrado con ID: " + id
            ));
        return mapper.toResponseDTO(entity);
    }

    @Override
    public UsuarioResponseDTO findByUsuario(String usuario) {
        log.debug("Buscando usuario: {}", usuario);
        Usuario entity = repository.findByUsuarioWithRoles(usuario)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Usuario no encontrado: " + usuario
            ));
        return mapper.toResponseDTO(entity);
    }

    @Override
    public Page<UsuarioListDTO> findAll(Pageable pageable) {
        log.debug("Listando usuarios - Página: {}", pageable.getPageNumber());
        return repository.findAll(pageable).map(mapper::toListDTO);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO create(UsuarioCreateDTO dto) {
        log.info("Creando nuevo usuario: {}", dto.usuario());
        
        // Validar usuario único
        if (repository.existsByUsuario(dto.usuario())) {
            throw new BusinessException(
                "Ya existe un usuario con el código: " + dto.usuario()
            );
        }
        
        // Crear persona primero
        var personaDTO = personaService.create(dto.persona());
        
        // Crear usuario
        Usuario entity = mapper.toEntity(dto);
        entity.setPassword(passwordEncoder.encode(dto.password()));
        
        // Asignar persona
        Persona persona = new Persona();
        persona.setId(personaDTO.id());
        entity.setPersona(persona);
        
        // Asignar roles
        Set<Rol> roles = obtenerRolesPorIds(dto.rolesIds());
        roles.forEach(entity::addRol);
        
        Usuario saved = repository.save(entity);
        log.info("Usuario creado exitosamente con ID: {}", saved.getId());
        return mapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO update(Integer id, UsuarioUpdateDTO dto) {
        log.info("Actualizando usuario ID: {}", id);
        
        Usuario entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Usuario no encontrado con ID: " + id
            ));
        
        // Validar usuario único si cambió
        if (!entity.getUsuario().equals(dto.usuario())
            && repository.existsByUsuario(dto.usuario())) {
            throw new BusinessException(
                "Ya existe un usuario con el código: " + dto.usuario()
            );
        }
        
        mapper.updateEntity(dto, entity);
        
        // Actualizar roles si se proporcionaron
        if (dto.rolesIds() != null && !dto.rolesIds().isEmpty()) {
            entity.getRoles().clear();
            Set<Rol> roles = obtenerRolesPorIds(dto.rolesIds());
            roles.forEach(entity::addRol);
        }
        
        Usuario updated = repository.save(entity);
        log.info("Usuario actualizado exitosamente");
        return mapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void cambiarPassword(Integer id, CambioPasswordDTO dto) {
        log.info("Cambiando contraseña del usuario ID: {}", id);
        
        Usuario entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Usuario no encontrado con ID: " + id
            ));
        
        // Validar contraseña actual
        if (!passwordEncoder.matches(dto.passwordActual(), entity.getPassword())) {
            throw new BusinessException("La contraseña actual es incorrecta");
        }
        
        // Validar que las nuevas contraseñas coincidan
        if (!dto.passwordNueva().equals(dto.passwordConfirmacion())) {
            throw new BusinessException("Las contraseñas nuevas no coinciden");
        }
        
        entity.setPassword(passwordEncoder.encode(dto.passwordNueva()));
        repository.save(entity);
        log.info("Contraseña cambiada exitosamente");
    }

    @Override
    @Transactional
    public UsuarioResponseDTO cambiarEstado(Integer id, UsuarioEstadoDTO dto) {
        log.info("Cambiando estado del usuario ID: {} a activo={}", id, dto.activo());
        
        Usuario entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Usuario no encontrado con ID: " + id
            ));
        
        entity.setActivo(dto.activo());
        Usuario updated = repository.save(entity);
        
        log.info("Estado del usuario actualizado exitosamente");
        return mapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public UsuarioResponseDTO asignarRoles(Integer id, UsuarioRolesDTO dto) {
        log.info("Asignando roles al usuario ID: {}", id);
        
        Usuario entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Usuario no encontrado con ID: " + id
            ));
        
        // Limpiar roles actuales
        entity.getRoles().clear();
        
        // Asignar nuevos roles
        Set<Rol> roles = obtenerRolesPorIds(dto.rolesIds());
        roles.forEach(entity::addRol);
        
        Usuario updated = repository.save(entity);
        log.info("Roles asignados exitosamente");
        return mapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.info("Eliminando (soft delete) usuario ID: {}", id);
        
        Usuario entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "Usuario no encontrado con ID: " + id
            ));
        
        repository.delete(entity); // Soft delete
        log.info("Usuario eliminado exitosamente");
    }

    private Set<Rol> obtenerRolesPorIds(Set<Integer> rolesIds) {
        return rolesIds.stream()
            .map(idRol -> rolRepository.findById(idRol)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Rol no encontrado con ID: " + idRol
                )))
            .collect(Collectors.toSet());
    }
}