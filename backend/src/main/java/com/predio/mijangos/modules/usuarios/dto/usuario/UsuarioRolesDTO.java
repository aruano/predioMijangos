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

// ==================== ASIGNACIÓN DE ROLES DTO ====================

/**
 * DTO para asignar o actualizar roles de un usuario.
 */
@Builder
@Schema(description = "Datos para asignar roles a un usuario")
public record UsuarioRolesDTO(
    
    @NotEmpty(message = "Debe asignar al menos un rol")
    @Schema(description = "IDs de los roles a asignar", example = "[1, 2, 3]")
    Set<Integer> rolesIds
) {}