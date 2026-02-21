package com.pacifico.policy.api;

import com.pacifico.policy.api.dto.PolicyIssuanceRequest;
import com.pacifico.policy.api.dto.PolicyIssuanceResponse;
import com.pacifico.policy.application.IssuePolicyUseCase;
import com.pacifico.policy.domain.model.PolicyIssuanceCommand;
import com.pacifico.policy.domain.model.PolicyIssuanceResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/policies")
@RequiredArgsConstructor
@Slf4j
public class PolicyIssuanceController {

    private final IssuePolicyUseCase useCase;

    @PostMapping("/issue")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PolicyIssuanceResponse> issuePolicy(@RequestBody PolicyIssuanceRequest request) {
        log.info("Received policy issuance request: {}", request.requestId());

        var command = new PolicyIssuanceCommand(
                request.requestId(),
                request.productType(),
                request.premiumAmount(),
                request.premiumCurrency(),
                request.customerInfo().customerId(),
                request.customerInfo().customerName(),
                request.customerInfo().email()
        );

        return useCase.issuePolicy(command)
                .map(this::toResponse)
                .doOnSuccess(response -> log.info("Policy issuance successful: {}", response.policyId()))
                .doOnError(error -> log.error("Policy issuance failed for requestId: {}",
                        request.requestId(), error));
    }

    @GetMapping("/{requestId}")
    public Mono<PolicyIssuanceResponse> getPolicyByRequestId(@PathVariable String requestId) {
        log.info("Getting policy by requestId: {}", requestId);

        return useCase.findByRequestId(requestId)
                .map(this::toResponse)
                .doOnSuccess(response -> log.info("Policy found: {}", response.policyId()))
                .switchIfEmpty(Mono.error(new PolicyNotFoundException("Policy not found for requestId: " + requestId)));
    }

    private PolicyIssuanceResponse toResponse(PolicyIssuanceResult result) {
        return new PolicyIssuanceResponse(
                result.policyId(),
                result.requestId(),
                result.status().name(),
                result.productType(),
                result.premiumAmount(),
                result.premiumCurrency(),
                result.paymentAuthorizationId(),
                result.createdAt(),
                result.issuedAt()
        );
    }

    public static class PolicyNotFoundException extends RuntimeException {
        public PolicyNotFoundException(String message) {
            super(message);
        }
    }
}
