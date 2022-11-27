package com.weather.surf_service.exception;

public class DateToFarAwayException extends RuntimeException {
    public DateToFarAwayException(String message) {
        super(message);
    }
}
