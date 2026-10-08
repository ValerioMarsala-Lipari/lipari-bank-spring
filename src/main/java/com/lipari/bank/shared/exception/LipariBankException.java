package com.lipari.bank.shared.exception;

public class LipariBankException extends RuntimeException {

    public LipariBankException(String message) {
        super(message);
    }

    public LipariBankException(String message, Throwable cause) {
        super(message, cause);
    }
}
