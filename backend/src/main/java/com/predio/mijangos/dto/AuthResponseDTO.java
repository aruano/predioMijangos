package com.predio.mijangos.dto;

import com.predio.mijangos.model.Rol;
import java.util.Set;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {
    private String token;
    private String usuario;
    private Set<Rol> roles;
}
