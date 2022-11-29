package com.weather.surf_service.exception;

public class DateTooFarAwayException extends RuntimeException {
    public DateTooFarAwayException(String message) {
        super(message);
    }
}
