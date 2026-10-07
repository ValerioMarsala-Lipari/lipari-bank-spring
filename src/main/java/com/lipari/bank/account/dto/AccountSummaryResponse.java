package com.lipari.bank.account.dto;

import com.lipari.bank.account.entity.AccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Schema(description = "Summary of a bank account associated with a customer")
public record AccountSummaryResponse(
    Long id,
    String iban,
    BigDecimal balance,
    AccountStatus status,
    LocalDateTime createdAt
) {}