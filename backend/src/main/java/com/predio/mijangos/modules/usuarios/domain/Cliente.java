package com.predio.mijangos.modules.usuarios.domain;

import com.predio.mijangos.core.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Entidad Cliente - Representa un cliente del sistema.
 * Contiene una relación Many-to-One con Persona para la información personal.
 * 
 * @author Predio Mijangos Dev Team
 * @version 1.0
 * @since 2025-10
 */
@Entity
@Table(
    name = "TBL_Cliente",
    indexes = {
        @Index(name = "idx_cliente_persona", columnList = "id_persona"),
        @Index(name = "idx_cliente_created", columnList = "created_at"),
        @Index(name = "idx_cliente_deleted", columnList = "deleted_at")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = "persona")
public class Cliente extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Relación Many-to-One con Persona.
     * Un cliente está vinculado a una persona.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "id_persona",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_cliente_persona")
    )
    private Persona persona;

    /**
     * Observaciones adicionales sobre el cliente.
     * Puede incluir notas sobre preferencias, historial, etc.
     */
    @Size(max = 100, message = "Las observaciones no pueden exceder 100 caracteres")
    @Column(name = "observaciones", length = 100)
    private String observaciones;

    /**
     * Constructor de conveniencia.
     * 
     * @param persona Persona asociada al cliente
     */
    public Cliente(Persona persona) {
        this.persona = persona;
    }

    /**
     * Obtiene el nombre completo del cliente desde la persona asociada.
     * 
     * @return Nombre completo
     */
    @Transient
    public String getNombreCompleto() {
        return persona != null ? persona.getNombreCompleto() : "";
    }

    /**
     * Obtiene la identificación del cliente desde la persona asociada.
     * 
     * @return Número de identificación
     */
    @Transient
    public String getIdentificacion() {
        return persona != null ? persona.getIdentificacion() : "";
    }

    /**
     * Obtiene el correo del cliente desde la persona asociada.
     * 
     * @return Correo electrónico
     */
    @Transient
    public String getCorreo() {
        return persona != null ? persona.getCorreo() : "";
    }

    /**
     * Obtiene el teléfono del cliente desde la persona asociada.
     * 
     * @return Número de teléfono
     */
    @Transient
    public String getTelefono() {
        return persona != null ? persona.getTelefono() : "";
    }
}