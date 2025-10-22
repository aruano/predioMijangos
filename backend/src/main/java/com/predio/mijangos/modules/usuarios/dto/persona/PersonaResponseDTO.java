package com.predio.mijangos.modules.usuarios.dto.persona;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;

// ==================== RESPONSE DTO ====================

/**
 * DTO de respuesta para Persona.
 */
@Builder
@Schema(description = "Información completa de una persona")
public record PersonaResponseDTO(
    
    @Schema(description = "ID único de la persona", example = "1")
    Integer id,
    
    @Schema(description = "Tipo de identificación", example = "DPI")
    String tipoIdentificacion,
    
    @Schema(description = "Número de identificación", example = "1234567890101")
    String identificacion,
    
    @Schema(description = "Nombre(s) de la persona", example = "Juan Carlos")
    String nombres,
    
    @Schema(description = "Apellido(s) de la persona", example = "García López")
    String apellidos,
    
    @Schema(description = "Nombre completo", example = "Juan Carlos García López")
    String nombreCompleto,
    
    @Schema(description = "Correo electrónico", example = "juan.garcia@example.com")
    String correo,
    
    @Schema(description = "Número telefónico", example = "50212345678")
    String telefono,
    
    @Schema(description = "ID del municipio", example = "1")
    Integer idMunicipio,
    
    @Schema(description = "Nombre del municipio", example = "Guatemala")
    String nombreMunicipio,
    
    @Schema(description = "Nombre del departamento", example = "Guatemala")
    String nombreDepartamento,
    
    @Schema(description = "Dirección de domicilio", example = "Zona 10, Ciudad de Guatemala")
    String direccionDomicilio,
    
    @Schema(description = "Fecha de creación", example = "2025-10-10T10:30:00")
    LocalDateTime createdAt,
    
    @Schema(description = "Fecha de última actualización", example = "2025-10-10T15:45:00")
    LocalDateTime updatedAt
) {}