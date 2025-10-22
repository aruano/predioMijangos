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
 * DTO para actualizar un Cliente.
 */
@Builder
@Schema(description = "Datos para actualizar un cliente")
public record ClienteUpdateDTO(
    
    @NotNull(message = "El ID de la persona es obligatorio")
    @Schema(description = "ID de la persona asociada", example = "1")
    Integer idPersona,
    
    @Size(max = 100, message = "Las observaciones no pueden exceder 100 caracteres")
    @Schema(description = "Observaciones sobre el cliente", example = "Cliente preferente")
    String observaciones
) {}