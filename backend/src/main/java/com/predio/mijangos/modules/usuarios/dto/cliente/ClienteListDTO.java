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
 * DTO simplificado para listados de clientes.
 */
@Builder
@Schema(description = "Información resumida de un cliente para listados")
public record ClienteListDTO(
    
    @Schema(description = "ID único del cliente", example = "1")
    Integer id,
    
    @Schema(description = "Nombre completo", example = "María López Pérez")
    String nombreCompleto,
    
    @Schema(description = "Identificación", example = "9876543210101")
    String identificacion,
    
    @Schema(description = "Correo electrónico", example = "maria.lopez@example.com")
    String correo,
    
    @Schema(description = "Teléfono", example = "50223456789")
    String telefono
) {}