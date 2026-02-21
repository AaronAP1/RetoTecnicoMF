package com.pacifico.payment.infrastructure.config;

import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Configuration
public class ObservabilityConfig {

    public WebFilter correlationIdFilter() {
        return (exchange, chain) -> {

            String traceId = UUID.randomUUID().toString();

            MDC.put("traceId", traceId);

            exchange.getResponse().getHeaders().add("X-Trace-Id", traceId);

            return chain.filter(exchange)
                    .doFinally(signal -> MDC.clear());
        };
    }
}