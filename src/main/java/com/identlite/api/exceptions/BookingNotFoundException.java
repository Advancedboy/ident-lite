package com.identlite.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException(Long id) {
        super("Booking with id " + id + " not found");
    }

    public BookingNotFoundException(String message) {
        super(message);
    }

    public BookingNotFoundException(Long id, Throwable cause) {
        super("Booking with id " + id + " not found", cause);
    }
}
