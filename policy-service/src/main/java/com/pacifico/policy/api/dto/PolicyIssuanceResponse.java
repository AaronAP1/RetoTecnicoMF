package com.pacifico.policy.api.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record PolicyIssuanceResponse(
        String policyId,
        String requestId,
        String status,
        String productType,
        BigDecimal premiumAmount,
        String premiumCurrency,
        String paymentAuthorizationId,
        Instant createdAt,
        Instant issuedAt
) {}
