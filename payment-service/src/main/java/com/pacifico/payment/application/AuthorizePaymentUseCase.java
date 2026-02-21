package com.pacifico.payment.application;

import com.pacifico.payment.domain.model.PaymentAuthorizationCommand;
import com.pacifico.payment.domain.model.PaymentAuthorizationResult;
import com.pacifico.payment.domain.rules.PaymentAuthorizationRulesEngine;
import com.pacifico.payment.infrastructure.persistence.PaymentAuthorizationRepository;
import com.pacifico.payment.infrastructure.persistence.PaymentMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Clock;

@Service
public class AuthorizePaymentUseCase {

    private final PaymentAuthorizationRepository repository;
    private final PaymentMapper mapper;
    private final PaymentAuthorizationRulesEngine engine;

    public AuthorizePaymentUseCase(PaymentAuthorizationRepository repository,
                                   PaymentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
        this.engine = new PaymentAuthorizationRulesEngine(
                PaymentAuthorizationRulesEngine.defaultRules(),
                Clock.systemUTC()
        );
    }

    public Mono<PaymentAuthorizationResult> authorize(PaymentAuthorizationCommand command) {

        var result = engine.authorize(command);

        var entity = mapper.toEntity(command, result);

        return repository.save(entity)
                .thenReturn(result);
    }
}