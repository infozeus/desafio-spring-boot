package com.nuevospa.taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task Service API")
                        .description("Task Service API Description")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Nuevo SPA")
                                .url("https://nuevospa.com")
                                .email("apis@nuevospa.com"))
                        .termsOfService("http://nuevospa.com/terms")
                        .license(new License()
                                .name("LICENSE")
                                .url("LICENSE URL")));
    }
}
