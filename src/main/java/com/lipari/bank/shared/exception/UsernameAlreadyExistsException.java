package com.lipari.bank.shared.exception;

public class UsernameAlreadyExistsException extends DuplicateResourceException {

    public UsernameAlreadyExistsException(String username) {
        super("Username already exists: " + username);
    }
}
