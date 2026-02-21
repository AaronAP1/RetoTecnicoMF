package com.pacifico.policy.api;

import com.pacifico.policy.infrastructure.payment.PaymentServiceClient;
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
                "message", "Internal server error",
                "path", "/policies/**",
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
                "path", "/policies/**",
                "timestamp", Instant.now().toEpochMilli(),
                "error", ex.getMessage()
        );

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }

    @ExceptionHandler(PolicyIssuanceController.PolicyNotFoundException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handlePolicyNotFoundException(
            PolicyIssuanceController.PolicyNotFoundException ex) {
        log.error("Policy not found", ex);

        Map<String, Object> errorResponse = Map.of(
                "message", "Policy not found",
                "path", "/policies/**",
                "timestamp", Instant.now().toEpochMilli(),
                "error", ex.getMessage()
        );

        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse));
    }

    @ExceptionHandler(PaymentServiceClient.PaymentServiceException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handlePaymentServiceException(
            PaymentServiceClient.PaymentServiceException ex) {
        log.error("Payment service error", ex);

        Map<String, Object> errorResponse = Map.of(
                "message", "Payment service unavailable",
                "path", "/policies/**",
                "timestamp", Instant.now().toEpochMilli(),
                "error", ex.getMessage()
        );

        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse));
    }
}
