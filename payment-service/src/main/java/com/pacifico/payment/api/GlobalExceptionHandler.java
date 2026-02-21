package com.pacifico.payment.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleGenericException(Exception ex) {
        log.error("Unhandled exception occurred", ex);

        Map<String, Object> errorResponse = Map.of(
                "message", "Internal error",
                "path", "/payments/authorize",
                "timestamp", Instant.now().toEpochMilli(),
                "error", ex.getMessage() != null ? ex.getMessage() : "Unknown error"
        );

        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleValidationException(IllegalArgumentException ex) {
        log.error("Validation error", ex);

        Map<String, Object> errorResponse = Map.of(
                "message", "Validation error",
                "path", "/payments/authorize",
                "timestamp", Instant.now().toEpochMilli(),
                "error", ex.getMessage()
        );

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }
}
