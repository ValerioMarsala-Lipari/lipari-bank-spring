package com.lipari.bank.shared.exception;

public class UsernameAlreadyExistsException extends LipariBankException {

    public UsernameAlreadyExistsException(String username) {
        super("Username already exists: " + username);
    }
}
