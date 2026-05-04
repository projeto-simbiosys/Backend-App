package com.simbiosys.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    // Cliente HTTP usado pelo GeminiAiService para chamar a API do Google
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // Mapper com suporte a LocalDateTime (necessário para serializar datas nas respostas)
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
