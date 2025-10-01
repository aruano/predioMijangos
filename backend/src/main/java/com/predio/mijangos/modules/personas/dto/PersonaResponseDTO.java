package com.predio.mijangos.modules.personas.dto;

public record PersonaResponseDTO(
    Integer id,
    String tipoIdentificacion,
    String identificacion,
    String nombres,
    String apellidos,
    String correo,
    String telefono,
    String direccionDomicilio,
    Integer idDepartamento,
    Integer idMunicipio
) {}
