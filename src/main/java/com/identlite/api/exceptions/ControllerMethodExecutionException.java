package com.identlite.api.exceptions;

public class ControllerMethodExecutionException extends RuntimeException {

    public ControllerMethodExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ControllerMethodExecutionException(String message) {
        super(message);
    }
}

