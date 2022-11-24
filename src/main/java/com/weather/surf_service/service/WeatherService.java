package com.weather.surf_service.service;

import com.weather.surf_service.model.LocationDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class WeatherService {

    private final WeatherBitService weatherBitService;

    public WeatherService(WeatherBitService weatherBitService) {
        this.weatherBitService = weatherBitService;
    }

    //This service will count the best location, WeatherBitService is to provide list of LocationDTO objects to count best weather here.
    public LocationDTO getBestWeather(String date) {
        //date YYYY-MM-DD
        checkDateFormat(date);
        return calculateBestWeather(getLocationListFromWeatherApi(date));
    }

    private LocationDTO calculateBestWeather(List<LocationDTO> locationListFromWeatherApi) {
        log.info("Calculating best weather.");
        //find the best Location -> return null or LocationDTO
        //TODO should return best LocationDTO object
        //if null -> log that thing
        return null;
    }

    private List<LocationDTO> getLocationListFromWeatherApi(String date) {
        return weatherBitService.getWeatherForLocations(date);
    }

    private void checkDateFormat(String date) {
        if (!isDateFormatCorrect(date)) {
            String message = String.format("Incorrect date format: %s", date);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    private boolean isDateFormatCorrect(String date) {
        return GenericValidator.isDate(date, "yyyy-MM-dd", true);
    }
}
