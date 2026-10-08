package com.lipari.bank.shared.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends LipariBankException {

    private final String iban;
    private final BigDecimal requested;
    private final BigDecimal available;

    public InsufficientFundsException(String iban, BigDecimal requested, BigDecimal available) {
        super("Insufficient funds on account %s: requested %.2f, available %.2f".formatted(iban, requested, available));
        this.iban = iban;
        this.requested = requested;
        this.available = available;
    }

    public String getIban() {
        return iban;
    }

    public BigDecimal getRequested() {
        return requested;
    }

    public BigDecimal getAvailable() {
        return available;
    }
}
