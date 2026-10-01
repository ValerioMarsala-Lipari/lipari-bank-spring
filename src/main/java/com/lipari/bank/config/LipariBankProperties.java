package com.lipari.bank.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "liparibank")
public record LipariBankProperties(
    String bankCode,
    BigDecimal maxTransferAmount,
    AuditProperties audit
) {

  public record AuditProperties(
      boolean enabled
  ) {
  }
}