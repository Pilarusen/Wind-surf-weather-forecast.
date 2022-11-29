package com.weather.surf_service.service;

import com.weather.surf_service.exception.NoneLocationMeetRequirementsException;
import com.weather.surf_service.exception.WrongDateFormatException;
import com.weather.surf_service.model.LocationDTO;
import com.weather.surf_service.model.LocationMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WeatherService {
    //TODO move date pattern to yml
    private final static String DATE_PATTERN = "yyyy-MM-dd";
    //TODO move below to yml
    int minWindSpeedRequirement = 5;
    int maxWindSpeedRequirement = 18;
    int minTempSpeedRequirement = 5;
    int maxTempSpeedRequirement = 35;

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

    private List<LocationDTO> getLocationListFromWeatherApi(String date) {
        return weatherBitService.getWeatherForLocations(date);
    }

    LocationMapper findBestWeather(List<LocationDTO> locationListFromWeatherApi) {
        log.info("Calculating best weather.");
        List<LocationMapper> locationsMeetRequirements = locationListFromWeatherApi
                .stream()
                .map(locationDTO -> LocationMapper.builder()
                        .cityName(locationDTO.getCityName())
                        .date(locationDTO.getValidDate())
                        .temperature(Float.parseFloat(locationDTO.getTemp()))
                        .windSpeed(Float.parseFloat(locationDTO.getWindSpeed()))
                        .build())
                .filter(locationMapper -> locationMapper.getWindSpeed() > minWindSpeedRequirement && locationMapper.getWindSpeed() < maxWindSpeedRequirement)
                .filter(locationMapper -> locationMapper.getTemperature() > minTempSpeedRequirement && locationMapper.getTemperature() < maxTempSpeedRequirement)
                .toList();
        if (locationsMeetRequirements.isEmpty()) {
            String message = "Non of the locations meets the requirements.";
            log.info(message);
            throw new NoneLocationMeetRequirementsException(message);
        } else if (locationsMeetRequirements.size() == 1) {
            LocationMapper locationResult = locationsMeetRequirements
                    .stream()
                    .findFirst()
                    .get();
            //TODO exception? or get?
            log.info("Locations meet requirements count == 1, location city name: {}", locationResult.getCityName());
            return locationResult;
        } else {
            log.info("Calculating best location. Number of locations meet requirements: {} ", locationsMeetRequirements.size());
            return calculateBestLocation(locationsMeetRequirements);
        }
    }

    LocationMapper calculateBestLocation(List<LocationMapper> locationsList) {
        Map<Float, LocationMapper> map = new LinkedHashMap<>();
        //How to find best weather: wind * 3 + temp.
        locationsList.forEach(location ->
                map.put(
                        (location.getWindSpeed() * 3 + location.getTemperature()), location
                ));

        LocationMapper locationResult = map.entrySet()
                .stream()
                .max(Map.Entry.comparingByKey())
                .stream()
                .findFirst()
                .map(Map.Entry::getValue)
                .get();
        //TODO here i know that list has min 2 elements so 1 must be present, no exception needed
        log.info("After calculation best location city name: {}.", locationResult.getCityName());
        return locationResult;
    }

    private void checkDateFormat(String date) {
        if (!isDateFormatCorrect(date)) {
            String message = String.format("Incorrect date format: %s", date);
            log.error(message);
            throw new WrongDateFormatException(message);
        }
    }

    private boolean isDateFormatCorrect(String date) {
        return GenericValidator.isDate(date, DATE_PATTERN, true);
    }
}
