package com.lipari.bank.shared.exception;

public class AccountNotFoundException extends LipariBankException {

    public AccountNotFoundException(String identifier) {
        super("Account not found: " + identifier);
    }
}
