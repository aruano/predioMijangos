package com.predio.mijangos.modules.security.dto;

import java.util.List;

/** DTO de módulo con sus páginas. */
public record MenuDTO(
  Integer id,
  String nombre,
  List<PageDTO> paginas
) {}
