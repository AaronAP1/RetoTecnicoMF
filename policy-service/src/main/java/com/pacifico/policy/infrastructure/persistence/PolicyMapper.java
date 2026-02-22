package com.pacifico.policy.infrastructure.persistence;

import com.pacifico.policy.domain.model.PolicyIssuanceCommand;
import com.pacifico.policy.domain.model.PolicyIssuanceResult;
import com.pacifico.policy.domain.model.PolicyStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PolicyMapper {

    public PolicyEntity toEntity(PolicyIssuanceResult result) {
        return new PolicyEntity(
                result.policyId(),
                result.requestId(),
                result.status().name(),
                result.productType(),
                result.premiumAmount(),
                result.premiumCurrency(),
                result.paymentRequestId(),
                result.paymentAuthorizationId(),
                result.createdAt(),
                result.issuedAt()
        );
    }

    public PolicyEntity toEntityForUpdate(PolicyIssuanceResult result) {
        return new PolicyEntity(
                result.policyId(),
                result.requestId(),
                result.status().name(),
                result.productType(),
                result.premiumAmount(),
                result.premiumCurrency(),
                result.paymentRequestId(),
                result.paymentAuthorizationId(),
                result.createdAt(),
                result.issuedAt()
        );
    }

    public PolicyIssuanceResult toResult(PolicyEntity entity) {
        return new PolicyIssuanceResult(
                entity.policyId(),
                entity.requestId(),
                PolicyStatus.valueOf(entity.status()),
                entity.productType(),
                entity.premiumAmount(),
                entity.premiumCurrency(),
                entity.paymentRequestId(),
                entity.paymentAuthorizationId(),
                entity.createdAt(),
                entity.issuedAt()
        );
    }

    public String generatePolicyId() {
        return "POL" + UUID.randomUUID().toString().replace("-", "").substring(0, 29);
    }

    public String generatePaymentRequestId(String policyRequestId) {
        return "PAY-" + policyRequestId;
    }
}
