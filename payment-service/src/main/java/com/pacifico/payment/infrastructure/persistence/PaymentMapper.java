package com.pacifico.payment.infrastructure.persistence;

import com.pacifico.payment.domain.model.*;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

    public PaymentAuthorizationResult toResult(PaymentAuthorizationEntity entity) {
        return new PaymentAuthorizationResult(
                entity.authorizationId(),
                entity.requestId(),
                AuthorizationStatus.valueOf(entity.status()),
                entity.amount(),
                entity.currency(),
                entity.declineReason() != null ?
                        Optional.of(DeclineReason.valueOf(entity.declineReason())) :
                        Optional.empty(),
                entity.createdAt()
        );
    }
}
