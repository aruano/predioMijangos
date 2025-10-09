package com.predio.mijangos.core.exception;

/**
 * Excepción lanzada cuando un recurso solicitado no se encuentra en el sistema.
 * Resulta en una respuesta HTTP 404 (Not Found).
 * 
 * Se usa típicamente en operaciones de búsqueda por ID o criterios específicos
 * cuando el recurso no existe en la base de datos.
 * 
 * Ejemplos de uso:
 * - throw new ResourceNotFoundException("Producto no encontrado con ID: 123");
 * - throw new ResourceNotFoundException("Producto", "123");
 * - throw new ResourceNotFoundException("Producto", "SKU", "ABC-123");
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
public class ResourceNotFoundException extends BusinessException {

    /**
     * Constructor con mensaje simple.
     * 
     * @param message Mensaje descriptivo del recurso no encontrado
     */
    public ResourceNotFoundException(String message) {
        super("RESOURCE_NOT_FOUND", message);
    }

    /**
     * Constructor con nombre de recurso e identificador.
     * Genera un mensaje formateado: "{recurso} no encontrado con ID: {id}".
     * 
     * @param resourceName Nombre del recurso (ej: "Producto", "Cliente")
     * @param resourceId Identificador del recurso
     */
    public ResourceNotFoundException(String resourceName, Object resourceId) {
        super("RESOURCE_NOT_FOUND", 
              String.format("%s no encontrado con ID: %s", resourceName, resourceId));
    }

    /**
     * Constructor con nombre de recurso, campo y valor.
     * Genera un mensaje formateado: "{recurso} no encontrado con {campo}: {valor}".
     * Útil para búsquedas por campos diferentes al ID.
     * 
     * @param resourceName Nombre del recurso (ej: "Producto", "Cliente")
     * @param fieldName Nombre del campo de búsqueda (ej: "SKU", "email")
     * @param fieldValue Valor buscado
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super("RESOURCE_NOT_FOUND", 
              String.format("%s no encontrado con %s: %s", resourceName, fieldName, fieldValue));
    }

    /**
     * Constructor con mensaje y causa original.
     * 
     * @param message Mensaje descriptivo del error
     * @param cause Excepción original que causó este error
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super("RESOURCE_NOT_FOUND", message, cause);
    }
}