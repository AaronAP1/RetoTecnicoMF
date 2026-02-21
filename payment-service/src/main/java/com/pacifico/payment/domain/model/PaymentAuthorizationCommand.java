package com.pacifico.payment.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public record PaymentAuthorizationCommand(
        String requestId,
        BigDecimal amount,
        String currency
) {
    public PaymentAuthorizationCommand {
        Objects.requireNonNull(requestId, "requestId is required");
        Objects.requireNonNull(amount, "amount is required");
        Objects.requireNonNull(currency, "currency is required");

        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be > 0");
        }
        if (currency.length() != 3) {
            throw new IllegalArgumentException("currency must be ISO-4217 like PEN/USD");
        }
    }
}