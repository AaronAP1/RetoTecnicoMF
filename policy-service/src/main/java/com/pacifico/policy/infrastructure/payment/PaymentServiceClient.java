package com.pacifico.policy.infrastructure.payment;

import com.pacifico.policy.infrastructure.payment.dto.PaymentAuthorizationRequest;
import com.pacifico.policy.infrastructure.payment.dto.PaymentAuthorizationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceClient {

    @Qualifier("paymentWebClient")
    private final WebClient paymentWebClient;

    public Mono<PaymentAuthorizationResponse> authorizePayment(PaymentAuthorizationRequest request) {
        log.info("Requesting payment authorization for requestId: {}", request.requestId());

        return paymentWebClient
                .post()
                .uri("/payments/authorize")
                .bodyValue(request)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> {
                    log.error("Client error when calling payment service: {}", response.statusCode());
                    return response.bodyToMono(String.class)
                            .map(body -> new PaymentServiceException(
                                    "Payment service client error: " + response.statusCode() + " - " + body))
                            .cast(Throwable.class);
                })
                .onStatus(status -> status.is5xxServerError(), response -> {
                    log.error("Server error when calling payment service: {}", response.statusCode());
                    return response.bodyToMono(String.class)
                            .map(body -> new PaymentServiceException(
                                    "Payment service server error: " + response.statusCode() + " - " + body))
                            .cast(Throwable.class);
                })
                .bodyToMono(PaymentAuthorizationResponse.class)
                .timeout(Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(2, Duration.ofMillis(500))
                        .filter(throwable -> !(throwable instanceof PaymentServiceException))
                )
                .doOnSuccess(response -> log.info("Payment authorization successful for requestId: {}, authId: {}",
                        request.requestId(), response.authorizationId()))
                .doOnError(error -> log.error("Failed to authorize payment for requestId: {}",
                        request.requestId(), error));
    }

    public static class PaymentServiceException extends RuntimeException {
        public PaymentServiceException(String message) {
            super(message);
        }
    }
}


