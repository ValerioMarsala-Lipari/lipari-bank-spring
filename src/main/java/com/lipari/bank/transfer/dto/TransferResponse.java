package com.lipari.bank.transfer.dto;

import com.lipari.bank.transfer.entity.TransferStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResponse(
        Long id,
        String fromIban,
        String toIban,
        BigDecimal amount,
        String description,
        LocalDateTime executedAt,
        TransferStatus status) {}
