package com.predio.mijangos.modules.security.service.impl;

import com.predio.mijangos.modules.geo.repo.MunicipioRepository;
import com.predio.mijangos.modules.personas.domain.Persona;
import com.predio.mijangos.modules.personas.dto.PersonaResponseDTO;
import com.predio.mijangos.modules.personas.repo.PersonaRepository;
import com.predio.mijangos.modules.security.domain.Rol;
import com.predio.mijangos.modules.security.domain.Usuario;
import com.predio.mijangos.modules.security.dto.*;
import com.predio.mijangos.modules.security.repo.RolRepository;
import com.predio.mijangos.modules.security.repo.UsuarioRepository;
import com.predio.mijangos.modules.security.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

  private final UsuarioRepository usuarioRepo;
  private final PersonaRepository personaRepo;
  private final MunicipioRepository municipioRepo;
  private final RolRepository rolRepo;
  private final PasswordEncoder passwordEncoder;

  // ------------------- CREATE -------------------
  @Override @Transactional
  public UsuarioResponseDTO create(UsuarioCreateRequestDTO req) {
    if (usuarioRepo.existsByUsuarioIgnoreCase(req.usuario())) {
      throw new IllegalArgumentException("El nombre de usuario ya existe");
    }
    // Persona
    Persona persona = mapToPersona(req.persona());
    persona = personaRepo.save(persona);

    // Roles
    List<Rol> roles = rolRepo.findAllById(req.rolesIds());
    if (roles.isEmpty() && !req.rolesIds().isEmpty()) {
      throw new NoSuchElementException("Alguno de los roles no existe");
    }

    // Usuario
    Usuario u = new Usuario();
    u.setUsuario(req.usuario());
    u.setPassword(passwordEncoder.encode(req.password()));
    u.setPersona(persona);
    u.setActivo(req.activo() == null ? Boolean.TRUE : req.activo());
    u.setRoles(new HashSet<>(roles));

    u = usuarioRepo.save(u);
    return mapToResponse(u);
  }

  // ------------------- UPDATE -------------------
  @Override @Transactional
  public UsuarioResponseDTO update(Integer id, UsuarioUpdateRequestDTO req) {
    Usuario u = usuarioRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

    // Persona (si viene)
    if (req.persona() != null) {
      Persona persona = u.getPersona() != null ? u.getPersona() : new Persona();
      mergePersona(persona, req.persona());
      persona = personaRepo.save(persona);
      u.setPersona(persona);
    }

    // Password (si se envía)
    if (req.password() != null && !req.password().isBlank()) {
      u.setPassword(passwordEncoder.encode(req.password()));
    }

    // Roles (si vienen)
    if (req.rolesIds() != null) {
      List<Rol> roles = rolRepo.findAllById(req.rolesIds());
      u.setRoles(new HashSet<>(roles));
    }

    // Activo (si viene)
    if (req.activo() != null) u.setActivo(req.activo());

    u = usuarioRepo.save(u);
    return mapToResponse(u);
  }

  // ------------------- GET -------------------
  @Override @Transactional(readOnly = true)
  public UsuarioResponseDTO getById(Integer id) {
    Usuario u = usuarioRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
    // Forzar carga de relaciones si es LAZY
    u.getRoles().size();
    if (u.getPersona() != null && u.getPersona().getMunicipio() != null) u.getPersona().getMunicipio().getId();
    return mapToResponse(u);
  }

  // ------------------- LIST -------------------
  @Override @Transactional(readOnly = true)
  public Page<UsuarioResponseDTO> list(String q, Boolean activo, Pageable pageable) {
    Page<Usuario> page;
    if (q != null && !q.isBlank()) {
      page = usuarioRepo.findByUsuarioContainingIgnoreCase(q, pageable);
    } else if (activo != null) {
      page = usuarioRepo.findByActivo(activo, pageable);
    } else {
      page = usuarioRepo.findAll(pageable);
    }
    return page.map(this::mapToResponse);
  }

  // ------------------- ACTIVAR/DESACTIVAR -------------------
  @Override @Transactional
  public void activar(Integer id) {
    Usuario u = usuarioRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
    u.setActivo(true);
    usuarioRepo.save(u);
  }

  @Override @Transactional
  public void desactivar(Integer id) {
    Usuario u = usuarioRepo.findById(id).orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
    u.setActivo(false);
    usuarioRepo.save(u);
  }

  // ------------------- MAPEOS -------------------
  private UsuarioResponseDTO mapToResponse(Usuario u) {
    PersonaResponseDTO personaDTO = null;
    if (u.getPersona() != null) {
      personaDTO = new PersonaResponseDTO(
          u.getPersona().getId(),
          u.getPersona().getTipoIdentificacion(),
          u.getPersona().getIdentificacion(),
          u.getPersona().getNombres(),
          u.getPersona().getApellidos(),
          u.getPersona().getCorreo(),
          u.getPersona().getTelefono(),
          u.getPersona().getDireccionDomicilio(),
          u.getPersona().getMunicipio() != null ? u.getPersona().getMunicipio().getDepartamento().getId() : null,
          u.getPersona().getMunicipio() != null ? u.getPersona().getMunicipio().getId() : null
      );
    }
    var roles = u.getRoles().stream().map(r -> new RolDTO(r.getId(), r.getNombre(), r.isAdmin())).toList();
    return new UsuarioResponseDTO(u.getId(), u.getUsuario(), u.getActivo(), personaDTO, roles);
  }

  private Persona mapToPersona(PersonaResponseDTO dto) {
    if (dto == null) return null;
    var p = new Persona();
    mergePersona(p, dto);
    return p;
  }

  private void mergePersona(Persona p, PersonaResponseDTO dto) {
    if (dto == null) return;
    p.setTipoIdentificacion(dto.tipoIdentificacion());
    p.setIdentificacion(dto.identificacion());
    p.setNombres(dto.nombres());
    p.setApellidos(dto.apellidos());
    p.setCorreo(dto.correo());
    p.setTelefono(dto.telefono());
    p.setDireccionDomicilio(dto.direccionDomicilio());
    if (dto.idMunicipio()!= null) {
      var muni = municipioRepo.findById(dto.idMunicipio())
          .orElseThrow(() -> new NoSuchElementException("Municipio no encontrado"));
      p.setMunicipio(muni);
    } else {
      p.setMunicipio(null);
    }
  }
}
