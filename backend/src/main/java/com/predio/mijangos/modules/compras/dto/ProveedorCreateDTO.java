package com.predio.mijangos.modules.compras.dto;

import jakarta.validation.constraints.*;

/**
 * DTO de entrada para crear un proveedor.
 * Validado con Jakarta Bean Validation en capa controller.
 *
 * Reglas:
 * - nombre es obligatorio.
 * - correo (si viene) debe cumplir formato.
 */
public record ProveedorCreateDTO(
    @NotBlank @Size(max=120) String nombre,
    @Size(max=180) String direccion,
    @Pattern(regexp="^[0-9 +()-]{7,20}$") String telefono,
    @Pattern(regexp="^[0-9 +()-]{7,20}$") String celular,
    @Email String correo,
    @Size(max=300) String observaciones
) {}