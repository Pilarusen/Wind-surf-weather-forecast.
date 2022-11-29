package com.weather.surf_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = WrongDateFormatException.class)
    public ResponseEntity<Object> handleWrongDateFormatException(WrongDateFormatException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                exception.getMessage(),
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, status);
    }

    @ExceptionHandler(value = DateFromPastException.class)
    public ResponseEntity<Object> handleDateFromPastException(DateFromPastException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                exception.getMessage(),
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, status);
    }

    @ExceptionHandler(value = DateTooFarAwayException.class)
    public ResponseEntity<Object> handleDateTooFarAwayException(DateTooFarAwayException exception) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                exception.getMessage(),
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, status);
    }

    @ExceptionHandler(value = NoneLocationMeetsRequirementsException.class)
    public ResponseEntity<Object> handleNoneLocationMeetsRequirements(NoneLocationMeetsRequirementsException exception) {
        HttpStatus status = HttpStatus.NO_CONTENT;

        ApiException apiException = new ApiException(
                exception.getMessage(),
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, status);
    }

    @ExceptionHandler(value = WeatherApiUnavailableException.class)
    public ResponseEntity<Object> handleWhenApiIsNotWorking(WeatherApiUnavailableException exception) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiException apiException = new ApiException(
                exception.getMessage(),
                status,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, status);
    }
}
