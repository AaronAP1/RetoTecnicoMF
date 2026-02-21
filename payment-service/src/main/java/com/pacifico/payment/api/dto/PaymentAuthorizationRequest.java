package com.pacifico.payment.api.dto;

import java.math.BigDecimal;

public record PaymentAuthorizationRequest(
        String requestId,
        BigDecimal amount,
        String currency,
        String paymentMethod,
        String cardNumber
) {}