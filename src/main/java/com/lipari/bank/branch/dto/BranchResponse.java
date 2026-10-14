package com.lipari.bank.branch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Branch data returned by the API")
public record BranchResponse(
        @Schema(description = "Unique branch ID", example = "1")
        Long id,

        @Schema(description = "Branch name", example = "Roma Centro")
        String name,

        @Schema(description = "Branch street address", example = "Via del Corso 100")
        String address,

        @Schema(description = "Branch city", example = "Roma")
        String city,

        @Schema(description = "Branch phone number", example = "+390612345678")
        String phone) {}
