package com.migros.couriertracking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI courierTrackingApi() {

        return new OpenAPI()
                .info(new Info()
                        .title("Courier Tracking API")
                        .description("Courier location tracking and distance calculation service")
                        .version("1.0.0"));
    }
}
