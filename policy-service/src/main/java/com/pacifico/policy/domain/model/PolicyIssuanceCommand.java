package com.pacifico.policy.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public record PolicyIssuanceCommand(
        String requestId,
        String productType,
        BigDecimal premiumAmount,
        String premiumCurrency,
        String customerId,
        String customerName,
        String customerEmail
) {
    public PolicyIssuanceCommand {
        Objects.requireNonNull(requestId, "requestId is required");
        Objects.requireNonNull(productType, "productType is required");
        Objects.requireNonNull(premiumAmount, "premiumAmount is required");
        Objects.requireNonNull(premiumCurrency, "premiumCurrency is required");
        Objects.requireNonNull(customerId, "customerId is required");

        if (premiumAmount.signum() <= 0) {
            throw new IllegalArgumentException("premiumAmount must be > 0");
        }
        if (premiumCurrency.length() != 3) {
            throw new IllegalArgumentException("premiumCurrency must be ISO-4217 like USD/EUR");
        }
    }
}
