package com.predio.mijangos.core.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entidad base con auditoría completa y soft delete.
 * TODAS las entidades principales deben extender de esta clase.
 * 
 * Proporciona:
 * - Auditoría automática (createdAt, updatedAt, createdBy, updatedBy)
 * - Soft delete (deletedAt)
 * - Métodos de utilidad para gestión de estado
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE {h-schema}{h-table} SET deleted_at = NOW() WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public abstract class BaseEntity {

    /**
     * Fecha y hora de creación del registro.
     * Se establece automáticamente al crear la entidad.
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Fecha y hora de última actualización del registro.
     * Se actualiza automáticamente en cada modificación.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * ID del usuario que creó el registro.
     * Se establece automáticamente desde el contexto de seguridad.
     */
    @CreatedBy
    @Column(name = "created_by")
    private Integer createdBy;

    /**
     * ID del usuario que realizó la última actualización.
     * Se actualiza automáticamente desde el contexto de seguridad.
     */
    @LastModifiedBy
    @Column(name = "updated_by")
    private Integer updatedBy;

    /**
     * Fecha y hora de eliminación lógica (soft delete).
     * NULL indica que el registro está activo.
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * Verifica si la entidad está marcada como eliminada (soft delete).
     * 
     * @return true si deletedAt no es null, false en caso contrario
     */
    @Transient
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * Marca la entidad como activa, restaurándola de un soft delete.
     * Establece deletedAt a null.
     */
    public void restore() {
        this.deletedAt = null;
    }

    /**
     * Marca la entidad como eliminada (soft delete).
     * Establece deletedAt con la fecha y hora actual.
     */
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * Verifica si la entidad es nueva (no persistida aún).
     * Las clases hijas deben implementar este método basándose en su ID.
     * 
     * @return true si la entidad es nueva, false si ya está persistida
     */
    @Transient
    public abstract boolean isNew();
}