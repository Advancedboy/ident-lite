package com.identlite.api.exceptions;

public class LoggingAspectException extends RuntimeException {
    public LoggingAspectException(String message, Throwable cause) {
        super(message, cause);
    }
}

