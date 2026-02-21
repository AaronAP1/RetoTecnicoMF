package com.pacifico.payment.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

public record PaymentAuthorizationResult(
        String authorizationId,
        String requestId,
        AuthorizationStatus status,
        BigDecimal authorizedAmount,
        String currency,
        Optional<DeclineReason> declineReason,
        Instant createdAt
) {
    public static PaymentAuthorizationResult approved(String authorizationId, String requestId,
                                                      BigDecimal amount, String currency, Instant now) {
        return new PaymentAuthorizationResult(
                authorizationId,
                requestId,
                AuthorizationStatus.APPROVED,
                amount,
                currency,
                Optional.empty(),
                now
        );
    }

    public static PaymentAuthorizationResult declined(String authorizationId, String requestId,
                                                      String currency, DeclineReason reason, Instant now) {
        return new PaymentAuthorizationResult(
                authorizationId,
                requestId,
                AuthorizationStatus.DECLINED,
                BigDecimal.ZERO,
                currency,
                Optional.of(reason),
                now
        );
    }
}