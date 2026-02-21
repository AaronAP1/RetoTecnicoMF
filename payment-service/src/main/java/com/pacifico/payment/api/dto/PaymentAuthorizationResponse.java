package com.pacifico.payment.api.dto;

import java.time.Instant;

public record PaymentAuthorizationResponse(
        String authorizationId,
        String requestId,
        String status,
        String declineReason,
        Instant createdAt
) {}