package com.weather.surf_service.exception;

public class NoneLocationMeetsRequirementsException extends RuntimeException {
    public NoneLocationMeetsRequirementsException(String message) {
        super(message);
    }
}
