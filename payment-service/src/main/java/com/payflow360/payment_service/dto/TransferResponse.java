package com.payflow360.payment_service.dto;

import java.math.BigDecimal;

public record TransferResponse(
        String sourceAccountNumber,
        String destinationAccountNumber,
        BigDecimal amount,
        BigDecimal sourceBalance,
        BigDecimal destinationBalance,
        String status
) {
}
