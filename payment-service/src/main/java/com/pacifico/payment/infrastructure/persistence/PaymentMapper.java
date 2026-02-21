package com.pacifico.payment.infrastructure.persistence;

import com.pacifico.payment.domain.model.PaymentAuthorizationCommand;
import com.pacifico.payment.domain.model.PaymentAuthorizationResult;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PaymentMapper {

    public PaymentAuthorizationEntity toEntity(PaymentAuthorizationCommand command,
                                               PaymentAuthorizationResult result) {
        return new PaymentAuthorizationEntity(
                result.authorizationId(),  // Usar el ID del resultado
                command.requestId(),
                result.status().name(),
                command.amount(),
                command.currency(),
                result.declineReason().map(Enum::name).orElse(null),
                result.createdAt()
        );
    }
}
