package com.lipari.bank.shared.config;

import java.math.BigDecimal;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "liparibank")
public record LipariBankProperties(String bankCode, BigDecimal maxTransferAmount, AuditProperties audit) {

    public record AuditProperties(boolean enabled) {}
}
