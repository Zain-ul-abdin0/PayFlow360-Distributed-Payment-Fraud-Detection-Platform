package com.payflow360.payment_service.dto;
import com.payflow360.payment_service.entity.PaymentStatus;

import java.math.BigDecimal;

public record PaymentResponse(
        String transactionId,
        PaymentStatus status,
        BigDecimal amount,
        String currency
) {
}