package com.pacifico.policy.domain.model;

import java.math.BigDecimal;
import java.time.Instant;

public record PolicyIssuanceResult(
        String policyId,
        String requestId,
        PolicyStatus status,
        String productType,
        BigDecimal premiumAmount,
        String premiumCurrency,
        String paymentRequestId,
        String paymentAuthorizationId,
        Instant createdAt,
        Instant issuedAt
) {
    public static PolicyIssuanceResult pendingPayment(String policyId, String requestId,
                                                     String productType, BigDecimal premiumAmount,
                                                     String premiumCurrency, String paymentRequestId,
                                                     Instant createdAt) {
        return new PolicyIssuanceResult(
                policyId,
                requestId,
                PolicyStatus.PENDING_PAYMENT,
                productType,
                premiumAmount,
                premiumCurrency,
                paymentRequestId,
                null,
                createdAt,
                null
        );
    }

    public static PolicyIssuanceResult paymentAuthorized(PolicyIssuanceResult existing,
                                                        String paymentAuthorizationId) {
        return new PolicyIssuanceResult(
                existing.policyId(),
                existing.requestId(),
                PolicyStatus.PAYMENT_AUTHORIZED,
                existing.productType(),
                existing.premiumAmount(),
                existing.premiumCurrency(),
                existing.paymentRequestId(),
                paymentAuthorizationId,
                existing.createdAt(),
                null
        );
    }

    public static PolicyIssuanceResult issued(PolicyIssuanceResult existing, Instant issuedAt) {
        return new PolicyIssuanceResult(
                existing.policyId(),
                existing.requestId(),
                PolicyStatus.ISSUED,
                existing.productType(),
                existing.premiumAmount(),
                existing.premiumCurrency(),
                existing.paymentRequestId(),
                existing.paymentAuthorizationId(),
                existing.createdAt(),
                issuedAt
        );
    }

    public static PolicyIssuanceResult paymentDeclined(PolicyIssuanceResult existing) {
        return new PolicyIssuanceResult(
                existing.policyId(),
                existing.requestId(),
                PolicyStatus.PAYMENT_DECLINED,
                existing.productType(),
                existing.premiumAmount(),
                existing.premiumCurrency(),
                existing.paymentRequestId(),
                null,
                existing.createdAt(),
                null
        );
    }

    public PolicyIssuanceResult withUpdatedEntity() {
        return this; // Para indicar que es un update, no un insert
    }
}
