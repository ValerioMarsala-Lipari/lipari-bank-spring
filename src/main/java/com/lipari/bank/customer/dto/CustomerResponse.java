package com.lipari.bank.customer.dto;

import com.lipari.bank.customer.entity.CustomerStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Customer data returned by the API")
public record CustomerResponse(

    @Schema(description = "Unique customer ID", example = "42")
    Long id,

    @Schema(
        description = "Customer fiscal code",
        example = "RSSMRA80A01H501Z"
    )
    String fiscalCode,

    @Schema(
        description = "Customer first name",
        example = "Mario"
    )
    String firstName,

    @Schema(
        description = "Customer last name",
        example = "Rossi"
    )
    String lastName,

    @Schema(
        description = "Customer email address",
        example = "mario.rossi@example.com"
    )
    String email,

    @Schema(
        description = "Customer phone number",
        example = "+393331234567"
    )
    String phone,

    @Schema(
        description = "Customer status",
        example = "ACTIVE"
    )
    CustomerStatus status
) {
}