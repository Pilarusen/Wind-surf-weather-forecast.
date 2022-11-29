package com.weather.surf_service.exception;

public class WrongLocationCoordinate extends RuntimeException{

    public WrongLocationCoordinate(String message) {
        super(message);
    }
}
