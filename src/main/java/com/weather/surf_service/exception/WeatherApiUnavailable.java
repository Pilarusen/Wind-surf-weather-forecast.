package com.weather.surf_service.exception;

public class WeatherApiUnavailable extends RuntimeException {
    public WeatherApiUnavailable(String message) {
        super(message);
    }
}
