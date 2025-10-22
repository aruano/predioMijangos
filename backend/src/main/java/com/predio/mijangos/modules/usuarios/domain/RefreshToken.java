package com.predio.mijangos.modules.usuarios.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad RefreshToken - Almacena tokens de refresco para autenticación JWT.
 * Permite mantener sesiones activas sin requerir login constante.
 * No extiende BaseEntity porque tiene su propia gestión de fechas.
 * 
 * @author Predio Mijangos Dev Team
 * @version 1.0
 * @since 2025-10
 */
@Entity
@Table(
    name = "TBL_Refresh_Token",
    indexes = {
        @Index(name = "idx_refresh_token_token", columnList = "token"),
        @Index(name = "idx_refresh_token_usuario", columnList = "id_usuario"),
        @Index(name = "idx_refresh_token_expiry", columnList = "expiry_date"),
        @Index(name = "idx_refresh_token_revoked", columnList = "revoked")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "usuario")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Token único de refresco.
     * Se genera aleatoriamente y debe ser único en el sistema.
     */
    @NotBlank(message = "El token es obligatorio")
    @Size(max = 255, message = "El token no puede exceder 255 caracteres")
    @Column(name = "token", nullable = false, unique = true, length = 255)
    private String token;

    /**
     * Relación Many-to-One con Usuario.
     * Un refresh token pertenece a un usuario específico.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "id_usuario",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_refresh_token_usuario")
    )
    private Usuario usuario;

    /**
     * Fecha y hora de expiración del token.
     * Después de esta fecha, el token no es válido.
     */
    @NotNull(message = "La fecha de expiración es obligatoria")
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    /**
     * Información del dispositivo desde el cual se generó el token.
     * Útil para auditoría y gestión de sesiones.
     */
    @Size(max = 255, message = "La información del dispositivo no puede exceder 255 caracteres")
    @Column(name = "device_info", length = 255)
    private String deviceInfo;

    /**
     * Dirección IP desde la cual se generó el token.
     * Útil para seguridad y auditoría.
     */
    @Size(max = 45, message = "La dirección IP no puede exceder 45 caracteres")
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    /**
     * Fecha y hora de creación del token.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Indica si el token ha sido revocado.
     * Los tokens revocados no pueden ser utilizados.
     */
    @Column(name = "revoked", nullable = false)
    @Builder.Default
    private Boolean revoked = false;

    /**
     * Fecha y hora de revocación del token.
     * Solo tiene valor si revoked = true.
     */
    @Column(name = "revoked_at")
    private LocalDateTime revokedAt;

    /**
     * Constructor de conveniencia.
     * 
     * @param token Token único
     * @param usuario Usuario propietario
     * @param expiryDate Fecha de expiración
     */
    public RefreshToken(String token, Usuario usuario, LocalDateTime expiryDate) {
        this.token = token;
        this.usuario = usuario;
        this.expiryDate = expiryDate;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Verifica si el token ha expirado.
     * 
     * @return true si el token ha expirado
     */
    @Transient
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }

    /**
     * Verifica si el token es válido para uso.
     * Un token es válido si no está revocado y no ha expirado.
     * 
     * @return true si el token es válido
     */
    @Transient
    public boolean isValid() {
        return !revoked && !isExpired();
    }

    /**
     * Revoca el token, marcándolo como inválido.
     * Establece la fecha de revocación al momento actual.
     */
    public void revoke() {
        this.revoked = true;
        this.revokedAt = LocalDateTime.now();
    }

    /**
     * Obtiene el tiempo restante hasta la expiración en segundos.
     * 
     * @return Segundos hasta la expiración, o 0 si ya expiró
     */
    @Transient
    public long getSecondsUntilExpiry() {
        if (isExpired()) {
            return 0;
        }
        return java.time.Duration.between(LocalDateTime.now(), expiryDate).getSeconds();
    }
}