package com.weather.surf_service.exception;

public class WeatherApiUnavailableException extends RuntimeException {
    public WeatherApiUnavailableException(String message) {
        super(message);
    }
}
