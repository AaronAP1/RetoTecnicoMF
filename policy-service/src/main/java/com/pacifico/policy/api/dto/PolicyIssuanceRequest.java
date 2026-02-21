package com.pacifico.policy.api.dto;

import java.math.BigDecimal;

public record PolicyIssuanceRequest(
        String requestId,
        String productType,
        BigDecimal premiumAmount,
        String premiumCurrency,
        CustomerInfo customerInfo
) {
    public record CustomerInfo(
            String customerId,
            String customerName,
            String email
    ) {}
}
