package com.pacifico.payment.domain.rules;

import com.pacifico.payment.domain.model.DeclineReason;
import com.pacifico.payment.domain.model.PaymentAuthorizationCommand;
import com.pacifico.payment.domain.model.PaymentAuthorizationResult;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class PaymentAuthorizationRulesEngine {

    private final List<PaymentAuthorizationRule> rules;
    private final Clock clock;
    private final Supplier<String> idGenerator;

    public PaymentAuthorizationRulesEngine(List<PaymentAuthorizationRule> rules, Clock clock) {
        this(rules, clock, () -> "AUTH" + UUID.randomUUID().toString().replace("-", "").substring(0, 32));
    }

    public PaymentAuthorizationRulesEngine(List<PaymentAuthorizationRule> rules,
                                           Clock clock,
                                           Supplier<String> idGenerator) {
        this.rules = List.copyOf(rules);
        this.clock = clock;
        this.idGenerator = idGenerator;
    }

    public PaymentAuthorizationResult authorize(PaymentAuthorizationCommand cmd) {
        var now = clock.instant();
        var authorizationId = idGenerator.get();

        Optional<DeclineReason> decline = rules.stream()
                .map(rule -> rule.evaluate(cmd))
                .flatMap(Optional::stream)
                .findFirst();

        return decline
                .map(reason -> PaymentAuthorizationResult.declined(
                        authorizationId,
                        cmd.requestId(),
                        cmd.currency(),
                        reason,
                        now
                ))
                .orElseGet(() -> PaymentAuthorizationResult.approved(
                        authorizationId,
                        cmd.requestId(),
                        cmd.amount(),
                        cmd.currency(),
                        now
                ));
    }

    public static List<PaymentAuthorizationRule> defaultRules() {
        return List.of(
                // Decline si amount > 1000
                cmd -> cmd.amount().compareTo(new BigDecimal("1000.00")) > 0
                        ? Optional.of(DeclineReason.LIMIT_EXCEEDED)
                        : Optional.empty()
        );
    }
}