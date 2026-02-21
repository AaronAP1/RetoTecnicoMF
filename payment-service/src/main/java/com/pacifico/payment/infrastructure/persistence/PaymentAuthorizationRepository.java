package com.pacifico.payment.infrastructure.persistence;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PaymentAuthorizationRepository
        extends ReactiveCrudRepository<PaymentAuthorizationEntity, String> {

    Mono<PaymentAuthorizationEntity> findByRequestId(String requestId);
}