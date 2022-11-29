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
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                exception.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = DateFromPastException.class)
    public ResponseEntity<Object> handleDateFromPastException(DateFromPastException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                exception.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = DateToFarAwayException.class)
    public ResponseEntity<Object> handleDateToFarAwayException(DateToFarAwayException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        ApiException apiException = new ApiException(
                exception.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = NoneLocationMeetRequirementsException.class)
    public ResponseEntity<Object> handleNoneLocationMeetRequirements(NoneLocationMeetRequirementsException exception) {
        HttpStatus badRequest = HttpStatus.NO_CONTENT;

        ApiException apiException = new ApiException(
                exception.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = WrongLocationCoordinate.class)
    public ResponseEntity<Object> handleWrongLocationsCoordinates(WrongLocationCoordinate exception) {
        HttpStatus badRequest = HttpStatus.INTERNAL_SERVER_ERROR;

        ApiException apiException = new ApiException(
                exception.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, badRequest);
    }

    @ExceptionHandler(value = WeatherApiUnavailable.class)
    public ResponseEntity<Object> handleWhenApiIsNotWorking(WeatherApiUnavailable exception) {
        HttpStatus badRequest = HttpStatus.BAD_GATEWAY;

        ApiException apiException = new ApiException(
                exception.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(apiException, badRequest);
    }
}
