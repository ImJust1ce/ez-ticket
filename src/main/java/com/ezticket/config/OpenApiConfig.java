package com.ezticket.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ticketingOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("EZ Ticket API")
                        .description("Ticketing system — modular monolith (layered)")
                        .version("v1"));
    }
}
