package com.weather.surf_service.service;

import com.weather.surf_service.model.LocationDTO;
import com.weather.surf_service.model.LocationMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@Service
public class WeatherService {

    private final WeatherBitService weatherBitService;

    public WeatherService(WeatherBitService weatherBitService) {
        this.weatherBitService = weatherBitService;
    }

    //This service will count the best location, WeatherBitService is to provide list of LocationDTO objects to count best weather here.
    public LocationMapper getBestWeather(String date) {
        //date yyyy-MM-dd
        checkDateFormat(date);
        return findBestWeather(getLocationListFromWeatherApi(date));
    }

    private LocationMapper findBestWeather(List<LocationDTO> locationListFromWeatherApi) {
        log.info("Calculating best weather.");
        List<LocationMapper> locationsMeetRequirements = locationListFromWeatherApi
                .stream()
                .map(locationDTO -> LocationMapper.builder()
                        .cityName(locationDTO.getCityName())
                        .date(locationDTO.getValidDate())
                        .temperature(Float.parseFloat(locationDTO.getTemp()))
                        .windSpeed(Float.parseFloat(locationDTO.getWindSpeed()))
                        .build())
                .filter(locationMapper -> locationMapper.getWindSpeed() > 5 && locationMapper.getWindSpeed() < 18)
                .filter(locationMapper -> locationMapper.getTemperature() > 5 && locationMapper.getTemperature() < 35)
                .toList();
        if (locationsMeetRequirements.isEmpty()) {
            String message = "Non of the locations meets the requirements.";
            log.info(message);
            throw new NoSuchElementException(message);
            //TODO rzucać wyjątek? czy może jakiś 404?
        } else if (locationsMeetRequirements.size() == 1) {
            LocationMapper locationResult = locationsMeetRequirements
                    .stream()
                    .findFirst()
                    .orElseThrow();
            //TODO orElse czy get?
            log.info("Locations meet requirements count == 1, location city name: {}", locationResult.getCityName());
            return locationResult;
        } else {
                return calculateBestLocation(locationsMeetRequirements);
        }
    }

    private LocationMapper calculateBestLocation(List<LocationMapper> locationsList) {
        log.info("Calculating best location. Number of locations meet requirements: {} ", locationsList.size());
        Map<Float, LocationMapper> map = new LinkedHashMap<>();
        //How to find best weather: wind * 3 + temp.
        locationsList.forEach(location -> map.put(
                (location.getWindSpeed() * 3 + location.getTemperature()),
                location
                ));

        LocationMapper locationResult = map.entrySet()
                .stream()
                .max(Map.Entry.comparingByKey())
                .stream()
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseThrow();
        //TODO or else czy get?
        log.info("After calculation best location city name: {}.", locationResult.getCityName());
        return locationResult;
    }

    private List<LocationDTO> getLocationListFromWeatherApi(String date) {
        return weatherBitService.getWeatherForLocations(date);
    }

    private void checkDateFormat(String date) {
        if (!isDateFormatCorrect(date)) {
            String message = String.format("Incorrect date format: %s", date);
            log.error(message);
            throw new IllegalArgumentException(message);
            //TODO wyjąteg czy status? czy coś?
        }
    }

    private boolean isDateFormatCorrect(String date) {
        return GenericValidator.isDate(date, "yyyy-MM-dd", true);
    }
}
