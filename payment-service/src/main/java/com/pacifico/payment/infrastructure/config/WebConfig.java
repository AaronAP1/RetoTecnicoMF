package com.pacifico.payment.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

@Configuration
@EnableWebFlux
public class WebConfig {
    // Config mínima — Spring Boot ya autoconfigura WebFlux
    // Puedes extender si necesitas codecs o CORS
}