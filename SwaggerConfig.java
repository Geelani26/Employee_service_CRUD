package com.example.HRMS.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.headers.Header;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myOpenAPI() {

        Info info = new Info().title("HRMS ").version("2.0")
                .description("Welcome to HRMS Api!");


        return new OpenAPI().components(new Components()
                        .addHeaders("Authorization", new Header()
                                .description("Add Bearer Token")
                                .required(true)
                        )).info(info).addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .components(new Components().addSecuritySchemes("Authorization",
                        new SecurityScheme().name("Authorization").type(SecurityScheme.Type.HTTP).scheme("bearer")));
    }
}
