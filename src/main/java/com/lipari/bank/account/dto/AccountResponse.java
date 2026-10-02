package com.lipari.bank.account.dto;

import com.lipari.bank.account.entity.AccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Schema(description = "Bank account data returned by the API")
public record AccountResponse(

    @Schema(description = "Unique account ID", example = "42")
    Long id,

    @Schema(
        description = "Bank account IBAN",
        example = "IT60X0542811101000000123456"
    )
    String iban,

    @Schema(
        description = "Fiscal code of the account holder",
        example = "RSSMRA80A01H501Z"
    )
    String fiscalCode,

    @Schema(description = "Current account balance in EUR", example = "2500.00")
    BigDecimal balance,

    @Schema(description = "Account status", example = "ACTIVE")
    AccountStatus status,

    @Schema(
        description = "Account creation date and time",
        example = "2026-10-02T10:30:00"
    )
    LocalDateTime createdAt
) {
}