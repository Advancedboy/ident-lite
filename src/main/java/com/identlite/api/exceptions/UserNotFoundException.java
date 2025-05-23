package com.identlite.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User with id " + id + " not found");
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    UserNotFoundException(Long id, Throwable cause) {
        super("User with id " + id + " not found", cause);
    }
}
