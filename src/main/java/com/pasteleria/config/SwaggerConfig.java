package com.pasteleria.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingrese el token JWT en el formato: Bearer <token>")))
                .info(new Info()
                        .title("API Pastelería Mil Sabores")
                        .version("1.0.0")
                        .description("API REST para la gestión de la Pastelería Mil Sabores. " +
                                "Incluye endpoints para autenticación con JWT, gestión de productos, " +
                                "categorías, usuarios y pedidos con control de acceso basado en roles.")
                        .contact(new Contact()
                                .name("Pastelería Mil Sabores")
                                .email("contacto@pasteleriamilsabores.cl")));
    }
}
