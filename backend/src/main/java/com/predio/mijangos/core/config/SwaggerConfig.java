package com.predio.mijangos.core.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger/OpenAPI para documentación de la API.
 * 
 * Proporciona:
 * - Documentación interactiva de endpoints
 * - Pruebas de API desde el navegador
 * - Esquema de autenticación JWT
 * - Información de versión y contacto
 * 
 * Acceso: http://localhost:8080/swagger-ui.html
 * 
 * @author Equipo Técnico Predio Mijangos
 * @version 1.0.0
 * @since Octubre 2025
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Predio Mijangos - API REST",
                version = "1.0.0",
                description = """
                        Sistema de Gestión de Inventario y Ventas para Predio Mijangos.
                        
                        Esta API proporciona endpoints para:
                        - Gestión de inventario de repuestos automotrices
                        - Gestión de ventas (contado y crédito)
                        - Administración de clientes y proveedores
                        - Control de vehículos
                        - Reportería y análisis
                        - Gestión de usuarios y permisos
                        
                        **Autenticación:** Todos los endpoints (excepto /api/auth/**) requieren 
                        autenticación mediante token JWT en el header Authorization.
                        
                        **Formato:** Bearer {token}
                        """,
                contact = @Contact(
                        name = "Equipo Técnico Predio Mijangos",
                        email = "soporte@prediomijangos.com",
                        url = "https://prediomijangos.com"
                ),
                license = @License(
                        name = "Propietario",
                        url = "https://prediomijangos.com/license"
                )
        ),
        servers = {
                @Server(
                        description = "Servidor Local (Desarrollo)",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Servidor de Testing",
                        url = "https://test-api.prediomijangos.com"
                ),
                @Server(
                        description = "Servidor de Producción",
                        url = "https://api.prediomijangos.com"
                )
        }
)
@SecurityScheme(
        name = "bearer-jwt",
        description = """
                Autenticación mediante JSON Web Token (JWT).
                
                Para obtener un token:
                1. Usar el endpoint POST /api/auth/login con credenciales válidas
                2. Copiar el token de la respuesta
                3. Hacer clic en "Authorize" y pegar: Bearer {token}
                
                El token expira después de 8 horas. Usar /api/auth/refresh para renovarlo.
                """,
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
    
    // La configuración se realiza mediante anotaciones
    // No se requieren beans adicionales con springdoc-openapi
    
    /*
     * Notas de implementación:
     * 
     * 1. Todos los controladores deben usar @Tag para agrupar endpoints
     *    Ejemplo: @Tag(name = "Productos", description = "Gestión de productos")
     * 
     * 2. Todos los endpoints protegidos deben usar @SecurityRequirement
     *    Ejemplo: @SecurityRequirement(name = "bearer-jwt")
     * 
     * 3. Documentar parámetros con @Parameter
     *    Ejemplo: @Parameter(description = "ID del producto")
     * 
     * 4. Documentar respuestas con @ApiResponse
     *    Ejemplo: @ApiResponse(responseCode = "200", description = "Producto encontrado")
     * 
     * 5. Usar @Schema en DTOs para documentar campos
     *    Ejemplo: @Schema(description = "Nombre del producto", example = "Aceite 10W40")
     */
}