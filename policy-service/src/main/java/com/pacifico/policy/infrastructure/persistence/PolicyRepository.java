package com.pacifico.policy.infrastructure.persistence;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PolicyRepository extends ReactiveCrudRepository<PolicyEntity, String> {

    Mono<PolicyEntity> findByRequestId(String requestId);

    @Modifying
    @Query("INSERT INTO policies (policy_id, request_id, status, product_type, premium_amount, premium_currency, payment_request_id, payment_authorization_id, created_at, issued_at) VALUES (:#{#entity.policyId}, :#{#entity.requestId}, :#{#entity.status}, :#{#entity.productType}, :#{#entity.premiumAmount}, :#{#entity.premiumCurrency}, :#{#entity.paymentRequestId}, :#{#entity.paymentAuthorizationId}, :#{#entity.createdAt}, :#{#entity.issuedAt})")
    Mono<Void> insertPolicy(@Param("entity") PolicyEntity entity);
}
