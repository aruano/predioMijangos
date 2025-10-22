package com.predio.mijangos.modules.usuarios.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad Módulo - Representa un módulo funcional del sistema.
 * Agrupa páginas relacionadas (ej: Ventas, Inventario, Compras).
 * No extiende BaseEntity porque es un catálogo relativamente estático.
 * 
 * @author Predio Mijangos Dev Team
 * @version 1.0
 * @since 2025-10
 */
@Entity
@Table(
    name = "TBL_Modulo",
    indexes = {
        @Index(name = "idx_modulo_nombre", columnList = "nombre"),
        @Index(name = "idx_modulo_orden", columnList = "orden")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "paginas")
public class Modulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombre único del módulo.
     * Ejemplo: Seguridad, Inventario, Ventas, Compras
     */
    @NotBlank(message = "El nombre del módulo es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    /**
     * Descripción del módulo y su propósito.
     */
    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    @Column(name = "descripcion", length = 255)
    private String descripcion;

    /**
     * Orden de visualización en el menú.
     * Menor número = mayor prioridad.
     */
    @Column(name = "orden")
    @Builder.Default
    private Integer orden = 0;

    /**
     * Indica si el módulo está activo.
     * Los módulos inactivos no se muestran en el sistema.
     */
    @Column(name = "activo", nullable = false)
    @Builder.Default
    private Boolean activo = true;

    /**
     * Relación One-to-Many con Pagina.
     * Un módulo puede tener múltiples páginas.
     */
    @OneToMany(mappedBy = "modulo", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orden ASC")
    @Builder.Default
    private List<Pagina> paginas = new ArrayList<>();

    /**
     * Constructor de conveniencia.
     * 
     * @param nombre Nombre del módulo
     * @param descripcion Descripción del módulo
     */
    public Modulo(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    /**
     * Método helper para agregar una página.
     * Mantiene la sincronización bidireccional.
     * 
     * @param pagina Página a agregar
     */
    public void addPagina(Pagina pagina) {
        paginas.add(pagina);
        pagina.setModulo(this);
    }

    /**
     * Método helper para remover una página.
     * Mantiene la sincronización bidireccional.
     * 
     * @param pagina Página a remover
     */
    public void removePagina(Pagina pagina) {
        paginas.remove(pagina);
        pagina.setModulo(null);
    }

    /**
     * Obtiene la cantidad de páginas en el módulo.
     * 
     * @return Cantidad de páginas
     */
    @Transient
    public int getCantidadPaginas() {
        return paginas.size();
    }

    /**
     * Obtiene la cantidad de páginas activas en el módulo.
     * 
     * @return Cantidad de páginas activas
     */
    @Transient
    public long getCantidadPaginasActivas() {
        return paginas.stream()
            .filter(Pagina::getActivo)
            .count();
    }
}