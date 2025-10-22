package com.predio.mijangos.modules.usuarios.dto.persona;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;

// ==================== LIST DTO ====================

/**
 * DTO simplificado para listados de personas.
 */
@Builder
@Schema(description = "Información resumida de una persona para listados")
public record PersonaListDTO(
    
    @Schema(description = "ID único de la persona", example = "1")
    Integer id,
    
    @Schema(description = "Tipo de identificación", example = "DPI")
    String tipoIdentificacion,
    
    @Schema(description = "Número de identificación", example = "1234567890101")
    String identificacion,
    
    @Schema(description = "Nombre completo", example = "Juan Carlos García López")
    String nombreCompleto,
    
    @Schema(description = "Correo electrónico", example = "juan.garcia@example.com")
    String correo,
    
    @Schema(description = "Número telefónico", example = "50212345678")
    String telefono,
    
    @Schema(description = "Nombre del municipio", example = "Guatemala")
    String nombreMunicipio
) {}