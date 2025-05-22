package com.identlite.api.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("Ресурс не найден");
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }
}
