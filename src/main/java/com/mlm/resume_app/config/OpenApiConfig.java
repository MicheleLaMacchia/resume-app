package com.mlm.resume_app.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI resumeOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Resume API - Michele La Macchia")
                        .description("API per esporre i nodi del CV")
                        .version("1.0.0")
                );
    }
}
