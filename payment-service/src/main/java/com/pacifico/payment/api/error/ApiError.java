package com.pacifico.payment.api.error;

import java.time.Instant;

public record ApiError(
        String message,
        String path,
        Instant timestamp
) {}