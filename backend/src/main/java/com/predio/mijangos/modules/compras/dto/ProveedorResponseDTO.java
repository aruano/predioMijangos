package com.predio.mijangos.modules.compras.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProveedorResponseDTO {
  private Integer id;
  private String nombre;
  private String direccion;
  private String telefono;
  private String celular;
  private String correo;
  private String observaciones;
  private Boolean activo;
}
