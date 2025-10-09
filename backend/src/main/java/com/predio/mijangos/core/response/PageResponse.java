package com.predio.mijangos.core.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Respuesta paginada estandarizada para listados de la API.
 * Encapsula los datos paginados junto con metadatos de paginación.
 * 
 * Estructura de respuesta:
 * {
 *   "content": [...],
 *   "currentPage": 0,
 *   "totalPages": 10,
 *   "totalElements": 95,
 *   "pageSize": 10,
 *   "hasNext": true,
 *   "hasPrevious": false,
 *   "isFirst": true,
 *   "isLast": false
 * }
 * 
 * @param <T> Tipo de datos de los elementos de la página
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
@Getter
@Builder
public class PageResponse<T> {

    /**
     * Lista de elementos de la página actual.
     */
    private List<T> content;

    /**
     * Número de la página actual (basado en cero).
     */
    private int currentPage;

    /**
     * Número total de páginas disponibles.
     */
    private int totalPages;

    /**
     * Número total de elementos en todas las páginas.
     */
    private long totalElements;

    /**
     * Tamaño de la página (número de elementos por página).
     */
    private int pageSize;

    /**
     * Indica si existe una página siguiente.
     */
    private boolean hasNext;

    /**
     * Indica si existe una página anterior.
     */
    private boolean hasPrevious;

    /**
     * Indica si esta es la primera página.
     */
    private boolean isFirst;

    /**
     * Indica si esta es la última página.
     */
    private boolean isLast;

    /**
     * Indica si la página está vacía (sin elementos).
     */
    private boolean isEmpty;

    /**
     * Crea un PageResponse a partir de un objeto Page de Spring Data.
     * Este es el método factory principal para crear respuestas paginadas.
     * 
     * @param page Objeto Page de Spring Data JPA
     * @param <T> Tipo de los elementos
     * @return PageResponse con todos los metadatos de paginación
     */
    public static <T> PageResponse<T> of(Page<T> page) {
        return PageResponse.<T>builder()
                .content(page.getContent())
                .currentPage(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .pageSize(page.getSize())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .isEmpty(page.isEmpty())
                .build();
    }

    /**
     * Crea un PageResponse vacío.
     * Útil para respuestas cuando no hay datos disponibles.
     * 
     * @param <T> Tipo de los elementos
     * @return PageResponse vacío con valores por defecto
     */
    public static <T> PageResponse<T> empty() {
        return PageResponse.<T>builder()
                .content(List.of())
                .currentPage(0)
                .totalPages(0)
                .totalElements(0L)
                .pageSize(0)
                .hasNext(false)
                .hasPrevious(false)
                .isFirst(true)
                .isLast(true)
                .isEmpty(true)
                .build();
    }

    /**
     * Crea un PageResponse con una lista simple sin paginación real.
     * Útil cuando se tiene una lista completa pero se necesita mantener
     * la consistencia del formato de respuesta.
     * 
     * @param content Lista de elementos
     * @param <T> Tipo de los elementos
     * @return PageResponse con todos los elementos en una sola página
     */
    public static <T> PageResponse<T> ofList(List<T> content) {
        return PageResponse.<T>builder()
                .content(content)
                .currentPage(0)
                .totalPages(1)
                .totalElements(content.size())
                .pageSize(content.size())
                .hasNext(false)
                .hasPrevious(false)
                .isFirst(true)
                .isLast(true)
                .isEmpty(content.isEmpty())
                .build();
    }
}