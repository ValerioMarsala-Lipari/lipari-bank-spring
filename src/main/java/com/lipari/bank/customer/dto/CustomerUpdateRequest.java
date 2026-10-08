package com.lipari.bank.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Data required to update a customer")
public record CustomerUpdateRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Schema(description = "Customer email address", example = "mario.rossi@example.com")
        String email,

        @NotBlank(message = "Phone is required")
        @Schema(description = "Customer phone number", example = "+393331234567")
        String phone) {}
