package com.pacifico.policy.infrastructure.payment.dto;

import java.time.Instant;

public record PaymentAuthorizationResponse(
        String authorizationId,
        String requestId,
        String status,
        String declineReason,
        Instant createdAt
) {}
