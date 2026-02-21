package com.pacifico.payment.infrastructure.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("payment_authorizations")
public record PaymentAuthorizationEntity(
        @Id
        @Column("authorization_id")
        String authorizationId,

        @Column("request_id")
        String requestId,

        @Column("status")
        String status,

        @Column("amount")
        BigDecimal amount,

        @Column("currency")
        String currency,

        @Column("decline_reason")
        String declineReason,

        @Column("created_at")
        java.time.Instant createdAt,

        @Transient
        boolean isNew
) implements Persistable<String> {

    public PaymentAuthorizationEntity(String authorizationId, String requestId, String status,
                                      BigDecimal amount, String currency, String declineReason,
                                      java.time.Instant createdAt) {
        this(authorizationId, requestId, status, amount, currency, declineReason, createdAt, true);
    }

    @Override
    public String getId() {
        return authorizationId;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
