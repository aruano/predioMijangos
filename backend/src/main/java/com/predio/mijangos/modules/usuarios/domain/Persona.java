package com.predio.mijangos.modules.usuarios.domain;

import com.predio.mijangos.core.entity.BaseEntity;
import com.predio.mijangos.modules.geo.domain.Municipio;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Entidad Persona - Base compartida para Usuario, Cliente y Proveedor.
 * Contiene información personal común a todas estas entidades.
 * 
 * @author Predio Mijangos Dev Team
 * @version 1.0
 * @since 2025-10
 */
@Entity
@Table(
    name = "TBL_Persona",
    indexes = {
        @Index(name = "idx_persona_identificacion", columnList = "identificacion"),
        @Index(name = "idx_persona_nombres", columnList = "nombres"),
        @Index(name = "idx_persona_apellidos", columnList = "apellidos"),
        @Index(name = "idx_persona_correo", columnList = "correo"),
        @Index(name = "idx_persona_created", columnList = "created_at"),
        @Index(name = "idx_persona_deleted", columnList = "deleted_at")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = {"municipio"})
public class Persona extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Tipo de documento de identificación.
     * Valores permitidos: DPI, NIT, PASAPORTE
     */
    @NotBlank(message = "El tipo de identificación es obligatorio")
    @Pattern(regexp = "^(DPI|NIT|PASAPORTE)$", message = "Tipo de identificación inválido")
    @Column(name = "tipo_identificacion", nullable = false, length = 15)
    private String tipoIdentificacion;

    /**
     * Número de identificación único.
     * Para DPI: 13 dígitos
     * Para NIT: 8-9 dígitos + guión + dígito verificador
     */
    @NotBlank(message = "La identificación es obligatoria")
    @Size(max = 15, message = "La identificación no puede exceder 15 caracteres")
    @Column(name = "identificacion", nullable = false, unique = true, length = 15)
    private String identificacion;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden exceder 100 caracteres")
    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden exceder 100 caracteres")
    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Email(message = "El correo electrónico no es válido")
    @Size(max = 100, message = "El correo no puede exceder 100 caracteres")
    @Column(name = "correo", length = 100)
    private String correo;

    /**
     * Teléfono en formato guatemalteco.
     * Formato: +502XXXXXXXX o 502XXXXXXXX
     */
    @Pattern(
        regexp = "^(\\+?502)?[2-7][0-9]{7}$",
        message = "El teléfono debe ser un número guatemalteco válido"
    )
    @Size(max = 15, message = "El teléfono no puede exceder 15 caracteres")
    @Column(name = "telefono", length = 15)
    private String telefono;

    /**
     * Relación Many-to-One con Municipio.
     * Una persona reside en un municipio.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "id_municipio",
        foreignKey = @ForeignKey(name = "fk_persona_municipio")
    )
    private Municipio municipio;

    @Size(max = 250, message = "La dirección no puede exceder 250 caracteres")
    @Column(name = "direccion_domicilio", length = 250)
    private String direccionDomicilio;

    /**
     * Obtiene el nombre completo de la persona.
     * 
     * @return Nombres y apellidos concatenados
     */
    @Transient
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    /**
     * Verifica si la persona tiene correo electrónico.
     * 
     * @return true si tiene correo, false en caso contrario
     */
    @Transient
    public boolean tieneCorreo() {
        return correo != null && !correo.isBlank();
    }

    /**
     * Verifica si la persona tiene teléfono.
     * 
     * @return true si tiene teléfono, false en caso contrario
     */
    @Transient
    public boolean tieneTelefono() {
        return telefono != null && !telefono.isBlank();
    }
}