package com.lipari.bank.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "Data required to update a bank account")
public record AccountUpdateRequest(
        @NotNull(message = "Balance is required")
        @DecimalMin(value = "0.00", message = "Balance cannot be negative")
        @Schema(description = "New account balance in EUR", example = "2500.00")
        BigDecimal balance) {}
