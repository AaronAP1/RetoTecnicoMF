package com.pacifico.payment.domain.rules;

import com.pacifico.payment.domain.model.DeclineReason;
import com.pacifico.payment.domain.model.PaymentAuthorizationCommand;

import java.util.Optional;

@FunctionalInterface
public interface PaymentAuthorizationRule {
    Optional<DeclineReason> evaluate(PaymentAuthorizationCommand cmd);
}