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

// ==================== CREATE DTO ====================

/**
 * DTO para crear un nuevo Usuario con su Persona.
 */
@Builder
@Schema(description = "Datos para crear un usuario con su información personal")
public record UsuarioCreateDTO(
    
    @NotBlank(message = "El código de usuario es obligatorio")
    @Size(max = 100, message = "El usuario no puede exceder 100 caracteres")
    @Schema(description = "Código de usuario o username", example = "VEND001")
    String usuario,
    
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Schema(description = "Contraseña del usuario", example = "Password123!")
    String password,
    
    @Valid
    @NotNull(message = "Los datos de la persona son obligatorios")
    @Schema(description = "Información personal del usuario")
    PersonaCreateDTO persona,
    
    @NotEmpty(message = "Debe asignar al menos un rol")
    @Schema(description = "IDs de los roles asignados", example = "[2, 3]")
    Set<Integer> rolesIds
) {}