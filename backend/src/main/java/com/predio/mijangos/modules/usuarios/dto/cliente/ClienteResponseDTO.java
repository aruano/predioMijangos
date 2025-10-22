// ==================== CLIENTE DTOs ====================

package com.predio.mijangos.modules.usuarios.dto.cliente;

import com.predio.mijangos.modules.usuarios.dto.persona.PersonaCreateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;


/**
 * DTO de respuesta para Cliente.
 */
@Builder
@Schema(description = "Información completa de un cliente")
public record ClienteResponseDTO(
    
    @Schema(description = "ID único del cliente", example = "1")
    Integer id,
    
    @Schema(description = "ID de la persona asociada", example = "1")
    Integer idPersona,
    
    @Schema(description = "Nombre completo", example = "María López Pérez")
    String nombreCompleto,
    
    @Schema(description = "Tipo de identificación", example = "DPI")
    String tipoIdentificacion,
    
    @Schema(description = "Número de identificación", example = "9876543210101")
    String identificacion,
    
    @Schema(description = "Correo electrónico", example = "maria.lopez@example.com")
    String correo,
    
    @Schema(description = "Teléfono", example = "50223456789")
    String telefono,
    
    @Schema(description = "Dirección", example = "Zona 11, Ciudad de Guatemala")
    String direccion,
    
    @Schema(description = "Observaciones", example = "Cliente preferente")
    String observaciones,
    
    @Schema(description = "Fecha de creación", example = "2025-10-10T10:30:00")
    LocalDateTime createdAt,
    
    @Schema(description = "Fecha de última actualización", example = "2025-10-10T15:45:00")
    LocalDateTime updatedAt
) {}