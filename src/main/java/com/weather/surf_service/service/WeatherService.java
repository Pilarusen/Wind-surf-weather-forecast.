package com.weather.surf_service.service;

import com.weather.surf_service.model.Location;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WeatherService {

    private final WeatherBitService weatherBitService;

    public WeatherService(WeatherBitService weatherBitService) {
        this.weatherBitService = weatherBitService;
    }

    public Location getBestWeather(String date) {
        //date YYYY-MM-DD
        checkDateFormat(date);
        sendRequestToWeatherBit(date);
        //TODO
        return null;
    }

    private Location sendRequestToWeatherBit(String date) {
        return weatherBitService.callApi(date);
        //TODO
    }

    private void checkDateFormat(String date) {
        if (!isDateFormatCorrect(date)) {
            String message = "Incorrect date format: " + date;
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    private boolean isDateFormatCorrect(String date) {
        return GenericValidator.isDate(date, "yyyy-MM-dd", true);
    }
}
