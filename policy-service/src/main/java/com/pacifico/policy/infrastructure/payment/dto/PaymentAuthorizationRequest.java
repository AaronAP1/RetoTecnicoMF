package com.pacifico.policy.infrastructure.payment.dto;

import java.math.BigDecimal;

public record PaymentAuthorizationRequest(
        String requestId,
        BigDecimal amount,
        String currency,
        String paymentMethod,
        String cardNumber
) {}
