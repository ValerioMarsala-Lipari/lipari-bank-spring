package com.lipari.bank.transfer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank(message = "Source IBAN is required") String fromIban,

        @NotBlank(message = "Destination IBAN is required") String toIban,

        @NotNull(message = "Amount is required") @Positive(message = "Amount must be positive")
        BigDecimal amount,

        @Size(max = 140, message = "Description cannot exceed 140 characters")
        String description) {}
