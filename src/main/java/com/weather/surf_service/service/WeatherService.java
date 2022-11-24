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
        //date YYYY-MM-DD
        checkDateFormat(date);
        return findBestWeather(getLocationListFromWeatherApi(date));
    }

    private LocationMapper findBestWeather(List<LocationDTO> locationListFromWeatherApi) {
        log.info("Calculating best weather.");
        List<LocationMapper> locationsMeetingRequirements = locationListFromWeatherApi
                .stream()
                .map(locationDTO -> LocationMapper.builder()
                        .city_name(locationDTO.getCity_name())
                        .date(locationDTO.getValid_date())
                        .temperature(Float.parseFloat(locationDTO.getTemp()))
                        .wind_speed(Float.parseFloat(locationDTO.getWind_spd()))
                        .build())
                .filter(locationMapper -> locationMapper.getWind_speed() > 5 && locationMapper.getWind_speed() < 18)
                .filter(locationMapper -> locationMapper.getTemperature() > 5 && locationMapper.getTemperature() < 35).toList();
        if (locationsMeetingRequirements.isEmpty()) {
            String message = "Non of the locations meets the requirements.";
            log.info(message);
            throw new NoSuchElementException(message);
        } else if (locationsMeetingRequirements.size() == 1) {
            LocationMapper locationResult = locationsMeetingRequirements.stream().findFirst().orElseThrow();
            //orElse czy get?
            log.info("Locations meet requirements count == 1, location city name: {}", locationResult.getCity_name());
            return locationResult;
        } else {
                return calculateBestLocation(locationsMeetingRequirements);
        }
    }

    private LocationMapper calculateBestLocation(List<LocationMapper> locationsList) {
        log.info("Calculating best location. Number of locations meet requirements: {} ", locationsList.size());
        Map<Float, LocationMapper> map = new LinkedHashMap<>();
        //How to find best weather: wind * 3 + temp.
        locationsList.forEach(location -> map.put(
                (location.getWind_speed() * 3 + location.getTemperature()),
                location));

        LocationMapper locationResult = map.entrySet()
                .stream()
                .max(Map.Entry.comparingByKey())
                .stream().findFirst()
                .map(Map.Entry::getValue).orElseThrow();
        //or else czy get?
        log.info("After calculation best location city name: {}", locationResult.getCity_name());
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
        }
    }

    private boolean isDateFormatCorrect(String date) {
        return GenericValidator.isDate(date, "yyyy-MM-dd", true);
    }
}
