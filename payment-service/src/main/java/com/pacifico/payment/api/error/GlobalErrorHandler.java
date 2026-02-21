package com.pacifico.payment.api.error;

import com.pacifico.payment.domain.exception.PaymentDomainException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(PaymentDomainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ApiError> handleDomain(
            PaymentDomainException ex,
            ServerWebExchange exchange
    ) {
        return Mono.just(new ApiError(
                ex.getMessage(),
                exchange.getRequest().getPath().value(),
                Instant.now()
        ));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<ApiError> handleGeneric(
            Exception ex,
            ServerWebExchange exchange
    ) {
        return Mono.just(new ApiError(
                "Internal error",
                exchange.getRequest().getPath().value(),
                Instant.now()
        ));
    }
}