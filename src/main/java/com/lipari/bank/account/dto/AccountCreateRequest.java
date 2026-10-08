package com.lipari.bank.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Schema(description = "Data required to create a new bank account")
public record AccountCreateRequest(
        @NotBlank(message = "Account holder's fiscal code is required")
        @Schema(description = "Fiscal code of the account holder", example = "RSSMRA80A01H501Z")
        String fiscalCode,

        @NotBlank(message = "IBAN is required")
        @Schema(description = "Bank account IBAN", example = "IT60X0542811101000000123456")
        String iban,

        @NotNull(message = "Initial balance is required")
        @PositiveOrZero(message = "Initial balance cannot be negative")
        @Schema(description = "Initial account balance in EUR", example = "1000.00")
        BigDecimal initialBalance) {}
