package com.simbiosys.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI simbiosysOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SIMBIOSYS API")
                        .description("API para análise e previsão de vulnerabilidade social - SMADS São Paulo")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipe SIMBIOSYS")
                                .email("contato@simbiosys.com")));
    }
}
