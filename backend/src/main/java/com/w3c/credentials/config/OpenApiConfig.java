package com.w3c.credentials.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("W3C Verifiable Credentials API")
                .description("Spring Boot REST API for managing W3C Verifiable Credentials. " +
                           "This API provides endpoints for issuing, storing, and verifying " +
                           "digital credentials following W3C standards.")
                .version("1.0.0")
                .contact(new Contact()
                    .name("W3C Credentials Team")
                    .email("support@example.com")
                    .url("https://www.w3.org/TR/vc-data-model/"))
                .license(new License()
                    .name("MIT License")
                    .url("https://opensource.org/licenses/MIT")));
    }
}