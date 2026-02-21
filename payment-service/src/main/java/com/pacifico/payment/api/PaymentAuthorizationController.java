package com.pacifico.payment.api;

import com.pacifico.payment.api.dto.PaymentAuthorizationRequest;
import com.pacifico.payment.api.dto.PaymentAuthorizationResponse;
import com.pacifico.payment.application.AuthorizePaymentUseCase;
import com.pacifico.payment.domain.model.PaymentAuthorizationCommand;
import com.pacifico.payment.domain.model.PaymentAuthorizationResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentAuthorizationController {

    private final AuthorizePaymentUseCase useCase;

    @PostMapping("/authorize")
    public Mono<PaymentAuthorizationResponse> authorize(
            @RequestBody PaymentAuthorizationRequest request
    ) {
        log.info("Received payment authorization request: {}", request);

        PaymentAuthorizationCommand command =
                new PaymentAuthorizationCommand(
                        request.requestId(),
                        request.amount(),
                        request.currency()
                );

        return useCase.authorize(command)
                .map(this::toResponse)
                .doOnSuccess(response -> log.info("Payment authorized successfully: {}", response))
                .doOnError(error -> log.error("Error authorizing payment", error));
    }

    private PaymentAuthorizationResponse toResponse(PaymentAuthorizationResult result) {
        return new PaymentAuthorizationResponse(
                result.authorizationId(),
                result.requestId(),
                result.status().name(),
                result.declineReason()
                        .map(Enum::name)
                        .orElse(null),
                result.createdAt()
        );
    }
}