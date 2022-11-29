package com.weather.surf_service.exception;

public class DateFromPastException extends RuntimeException {
    public DateFromPastException(String message) {
        super(message);
    }
}
