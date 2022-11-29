package com.weather.surf_service.exception;

public class WrongLocationCoordinateException extends RuntimeException{

    public WrongLocationCoordinateException(String message) {
        super(message);
    }
}
