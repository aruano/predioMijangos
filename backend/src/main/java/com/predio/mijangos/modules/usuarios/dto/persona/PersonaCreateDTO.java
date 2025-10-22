package com.predio.mijangos.modules.usuarios.dto.persona;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;

// ==================== CREATE DTO ====================

/**
 * DTO para crear una nueva Persona.
 */
@Builder
@Schema(description = "Datos para crear una persona")
public record PersonaCreateDTO(
    
    @NotBlank(message = "El tipo de identificación es obligatorio")
    @Pattern(regexp = "^(DPI|NIT|PASAPORTE)$", message = "Tipo de identificación inválido")
    @Schema(description = "Tipo de identificación", example = "DPI", allowableValues = {"DPI", "NIT", "PASAPORTE"})
    String tipoIdentificacion,
    
    @NotBlank(message = "La identificación es obligatoria")
    @Size(max = 15, message = "La identificación no puede exceder 15 caracteres")
    @Schema(description = "Número de identificación", example = "1234567890101")
    String identificacion,
    
    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden exceder 100 caracteres")
    @Schema(description = "Nombre(s) de la persona", example = "Juan Carlos")
    String nombres,
    
    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden exceder 100 caracteres")
    @Schema(description = "Apellido(s) de la persona", example = "García López")
    String apellidos,
    
    @Email(message = "El correo electrónico no es válido")
    @Size(max = 100, message = "El correo no puede exceder 100 caracteres")
    @Schema(description = "Correo electrónico", example = "juan.garcia@example.com")
    String correo,
    
    @Pattern(
        regexp = "^(\\+?502)?[2-7][0-9]{7}$",
        message = "El teléfono debe ser un número guatemalteco válido"
    )
    @Size(max = 15, message = "El teléfono no puede exceder 15 caracteres")
    @Schema(description = "Número telefónico", example = "50212345678")
    String telefono,
    
    @Schema(description = "ID del municipio de residencia", example = "1")
    Integer idMunicipio,
    
    @Size(max = 250, message = "La dirección no puede exceder 250 caracteres")
    @Schema(description = "Dirección de domicilio", example = "Zona 10, Ciudad de Guatemala")
    String direccionDomicilio
) {}