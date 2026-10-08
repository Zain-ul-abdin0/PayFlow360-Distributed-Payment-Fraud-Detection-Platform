package com.payflow360.payment_service.dto;

import java.math.BigDecimal;

public record AccountResponse(
        String accountNumber,
        String ownerName,
        BigDecimal balance,
        String currency
) {
}