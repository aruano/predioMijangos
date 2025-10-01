package com.predio.mijangos.modules.compras.dto;

/**
 * DTO de salida para listados paginados de proveedores. Contiene el subconjunto
 * mínimo para tablas y autocompletados.
 *
 * Invariantes: - id no nulo en respuestas del backend. - activo nunca nulo
 * (default true).
 */
public record ProveedorListItemDTO(
        Integer id,
        String nombre,
        String telefono,
        String celular,
        Boolean activo
        ) {

}
