package com.predio.mijangos.modules.security.dto;

/** DTO de página/submódulo. */
public record PageDTO(
  Integer id,
  String nombre,
  Boolean movil,
  String icon,
  String redirect,
  Integer moduloId,
  String moduloNombre
) {}
