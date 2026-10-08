package com.lipari.bank.customer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Data required to create a new customer")
public record CustomerCreateRequest(
        @NotBlank(message = "Fiscal code is required")
        @Schema(description = "Customer fiscal code", example = "RSSMRA80A01H501Z")
        String fiscalCode,

        @NotBlank(message = "First name is required") @Schema(description = "Customer first name", example = "Mario")
        String firstName,

        @NotBlank(message = "Last name is required") @Schema(description = "Customer last name", example = "Rossi")
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Schema(description = "Customer email address", example = "mario.rossi@example.com")
        String email,

        @NotBlank(message = "Phone is required")
        @Schema(description = "Customer phone number", example = "+393331234567")
        String phone) {}
