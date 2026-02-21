package com.pacifico.policy.api.dto;

public record CustomerInfo(
        String customerId,
        String customerName,
        String email
) {
}
