package com.pacifico.policy.application;

import com.pacifico.policy.domain.model.PolicyIssuanceCommand;
import com.pacifico.policy.domain.model.PolicyIssuanceResult;
import com.pacifico.policy.infrastructure.payment.PaymentServiceClient;
import com.pacifico.policy.infrastructure.payment.dto.PaymentAuthorizationRequest;
import com.pacifico.policy.infrastructure.persistence.PolicyMapper;
import com.pacifico.policy.infrastructure.persistence.PolicyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class IssuePolicyUseCase {

    private final PolicyRepository repository;
    private final PolicyMapper mapper;
    private final PaymentServiceClient paymentServiceClient;
    private final Clock clock = Clock.systemUTC();

    public Mono<PolicyIssuanceResult> issuePolicy(PolicyIssuanceCommand command) {
        log.info("Starting policy issuance for requestId: {}", command.requestId());

        // Verificar si ya existe una póliza para este requestId
        return repository.findByRequestId(command.requestId())
                .doOnNext(existing -> log.info("Found existing policy for requestId: {}, returning it", command.requestId()))
                .map(mapper::toResult)
                .switchIfEmpty(Mono.defer(() -> createNewPolicy(command)))
                .doOnSuccess(result -> log.info("Policy issuance completed for requestId: {}, status: {}",
                        command.requestId(), result.status()))
                .doOnError(error -> log.error("Policy issuance failed for requestId: {}",
                        command.requestId(), error));
    }

    private Mono<PolicyIssuanceResult> createNewPolicy(PolicyIssuanceCommand command) {
        // 1. Crear la póliza inicial
        var policyId = mapper.generatePolicyId();
        var paymentRequestId = mapper.generatePaymentRequestId(command.requestId());
        var now = clock.instant();

        log.info("Creating new policy with ID: {} for requestId: {}", policyId, command.requestId());

        var initialResult = PolicyIssuanceResult.pendingPayment(
                policyId,
                command.requestId(),
                command.productType(),
                command.premiumAmount(),
                command.premiumCurrency(),
                paymentRequestId,
                now
        );

        // 2. Guardar póliza inicial
        return repository.save(mapper.toEntity(initialResult))
                .flatMap(savedEntity -> {
                    log.info("Policy created with ID: {}, proceeding with payment authorization", policyId);

                    // 3. Solicitar autorización de pago
                    var paymentRequest = new PaymentAuthorizationRequest(
                            paymentRequestId,
                            command.premiumAmount(),
                            command.premiumCurrency(),
                            "CARD", // Default payment method
                            "4111111111111111" // Default test card
                    );

                    return paymentServiceClient.authorizePayment(paymentRequest)
                            .flatMap(paymentResponse -> {
                                log.info("Payment authorization received: {}", paymentResponse.status());

                                if ("APPROVED".equals(paymentResponse.status())) {
                                    // 4. Actualizar póliza con autorización exitosa e emitir
                                    var authorizedResult = PolicyIssuanceResult.paymentAuthorized(
                                            initialResult, paymentResponse.authorizationId());
                                    var issuedResult = PolicyIssuanceResult.issued(authorizedResult, Instant.now(clock));

                                    return repository.save(mapper.toEntityForUpdate(issuedResult))
                                            .map(mapper::toResult);
                                } else {
                                    // 5. Actualizar póliza con pago rechazado
                                    var declinedResult = PolicyIssuanceResult.paymentDeclined(initialResult);
                                    return repository.save(mapper.toEntityForUpdate(declinedResult))
                                            .map(mapper::toResult);
                                }
                            })
                            .onErrorResume(error -> {
                                log.error("Payment authorization failed for policy: {}", policyId, error);
                                // En caso de error, marcar como pago rechazado
                                var declinedResult = PolicyIssuanceResult.paymentDeclined(initialResult);
                                return repository.save(mapper.toEntityForUpdate(declinedResult))
                                        .map(mapper::toResult);
                            });
                });
    }

    public Mono<PolicyIssuanceResult> findByRequestId(String requestId) {
        return repository.findByRequestId(requestId)
                .map(mapper::toResult);
    }
}
