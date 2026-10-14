package com.lipari.bank.shared.exception;

public class CustomerNotFoundException extends BusinessException {

    public CustomerNotFoundException(String identifier) {
        super("Customer not found: " + identifier);
    }
}
