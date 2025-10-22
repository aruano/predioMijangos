package com.predio.mijangos.modules.usuarios.dto.usuario;

import com.predio.mijangos.modules.usuarios.dto.persona.PersonaCreateDTO;
import com.predio.mijangos.modules.usuarios.dto.rol.RolSimpleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

// ==================== UPDATE DTO ====================

/**
 * DTO para actualizar un Usuario existente.
 * No incluye contraseña, esa se cambia en endpoint separado.
 */
@Builder
@Schema(description = "Datos para actualizar un usuario")
public record UsuarioUpdateDTO(
    
    @NotBlank(message = "El código de usuario es obligatorio")
    @Size(max = 100, message = "El usuario no puede exceder 100 caracteres")
    @Schema(description = "Código de usuario o username", example = "VEND001")
    String usuario,
    
    @Schema(description = "ID de la persona asociada", example = "1")
    Integer idPersona,
    
    @Schema(description = "IDs de los roles asignados", example = "[2, 3]")
    Set<Integer> rolesIds
) {}
