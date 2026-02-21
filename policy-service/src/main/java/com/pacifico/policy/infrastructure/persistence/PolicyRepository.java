package com.pacifico.policy.infrastructure.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PolicyRepository extends ReactiveCrudRepository<PolicyEntity, String> {

    Mono<PolicyEntity> findByRequestId(String requestId);

    Mono<PolicyEntity> findByPaymentRequestId(String paymentRequestId);
}
