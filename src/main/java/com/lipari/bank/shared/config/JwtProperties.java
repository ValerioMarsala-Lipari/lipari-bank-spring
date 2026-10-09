package com.lipari.bank.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "liparibank.jwt")
public record JwtProperties(String secret, long accessTokenExpiration, long refreshTokenExpiration) {}
