package com.weather.surf_service.exception;

public class WrongDateFormatException extends RuntimeException {
    public WrongDateFormatException(String message) {
        super(message);
    }

}
