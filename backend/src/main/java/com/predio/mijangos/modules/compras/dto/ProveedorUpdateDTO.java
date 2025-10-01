package com.predio.mijangos.modules.compras.dto;

import jakarta.validation.constraints.*;

/** Datos para actualizar proveedor (parciales). */
public record ProveedorUpdateDTO(
    @NotNull Integer id,
    @Size(max=120) String nombre,
    @Size(max=180) String direccion,
    @Pattern(regexp="^[0-9 +()-]{7,20}$") String telefono,
    @Pattern(regexp="^[0-9 +()-]{7,20}$") String celular,
    @Email String correo,
    @Size(max=300) String observaciones,
    Boolean activo
) {}
