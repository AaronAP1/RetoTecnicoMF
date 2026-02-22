package com.pacifico.policy.infrastructure.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Table("policies")
public record PolicyEntity(
        @Id
        @Column("policy_id")
        String policyId,

        @Column("request_id")
        String requestId,

        @Column("status")
        String status,

        @Column("product_type")
        String productType,

        @Column("premium_amount")
        BigDecimal premiumAmount,

        @Column("premium_currency")
        String premiumCurrency,

        @Column("payment_request_id")
        String paymentRequestId,

        @Column("payment_authorization_id")
        String paymentAuthorizationId,

        @Column("created_at")
        Instant createdAt,

        @Column("issued_at")
        Instant issuedAt
) {

}
