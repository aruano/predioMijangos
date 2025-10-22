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
 * DTO para crear un Cliente.
 */
@Builder
@Schema(description = "Datos para crear un cliente")
public record ClienteCreateDTO(
    
    @Valid
    @NotNull(message = "Los datos de la persona son obligatorios")
    @Schema(description = "Información personal del cliente")
    PersonaCreateDTO persona,
    
    @Size(max = 100, message = "Las observaciones no pueden exceder 100 caracteres")
    @Schema(description = "Observaciones sobre el cliente", example = "Cliente preferente")
    String observaciones
) {}