package com.lipari.bank.shared.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LipariBankStartupLogger {

    private static final Logger log = LoggerFactory.getLogger(LipariBankStartupLogger.class);

    private final LipariBankProperties properties;

    public LipariBankStartupLogger(LipariBankProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    void logConfiguration() {
        log.info("//------------------------------//");
        log.info("Bank code: {}", properties.bankCode());
        log.info("Max transfer amount: {}", properties.maxTransferAmount());
        log.info("Audit enabled: {}", properties.audit().enabled());
        log.info("//------------------------------//");
    }
}
