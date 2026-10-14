package com.lipari.bank.branch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Data required to create a new branch")
public record BranchCreateRequest(
        @NotBlank(message = "Branch name is required") @Schema(description = "Branch name", example = "Roma Centro")
        String name,

        @NotBlank(message = "Address is required")
        @Schema(description = "Branch street address", example = "Via del Corso 100")
        String address,

        @NotBlank(message = "City is required") @Schema(description = "Branch city", example = "Roma")
        String city,

        @NotBlank(message = "Phone is required") @Schema(description = "Branch phone number", example = "+390612345678")
        String phone) {}
