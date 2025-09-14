package com.myexercise.notes.notes_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class OpenApiConfig {

    private static final String TITLE = "Notes API";
    private static final String VERSION = "1.0";
    private static final String DESCRIPTION = "Tenant awareness API";
    private static final String TENANT_HEADER = "X-Tenant-Id";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(getInfo())
                .components(getComponent())
                .addSecurityItem(getSecurityRequirement());
    }

    private Info getInfo() {
        return new Info().title(TITLE).version(VERSION).description(DESCRIPTION);
    }

    private Components getComponent() {
        return new Components().addSecuritySchemes(TENANT_HEADER, addSecurityScheme());
    }

    private SecurityScheme addSecurityScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name(TENANT_HEADER);
    }

    private SecurityRequirement getSecurityRequirement() {
        return new SecurityRequirement().addList(TENANT_HEADER);
    }

}
