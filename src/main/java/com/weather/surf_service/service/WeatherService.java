package com.weather.surf_service.service;

import com.weather.surf_service.exception.NoneLocationMeetsRequirementsException;
import com.weather.surf_service.exception.WrongDateFormatException;
import com.weather.surf_service.model.LocationDTO;
import com.weather.surf_service.model.LocationMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.weather.surf_service.util.Utils.DATE_PATTERN;

@AllArgsConstructor
@Slf4j
@Service
public class WeatherService {
    private final static int minWindSpeedRequirement = 5;
    private final static int maxWindSpeedRequirement = 18;
    private final static int minTempSpeedRequirement = 5;
    private final static int maxTempSpeedRequirement = 35;

    private final WeatherBitService weatherBitService;

    //This service will count the best location,
    //WeatherBitService is to provide list of LocationDTO objects to count best weather here.
    public LocationMapper getBestWeather(String date) {
        checkDateFormat(date);
        return findBestWeather(getLocationListFromWeatherApi(date));
    }

    private List<LocationDTO> getLocationListFromWeatherApi(String date) {
        return weatherBitService.getWeatherForLocations(date);
    }

    LocationMapper findBestWeather(List<LocationDTO> locationListFromWeatherApi) {
        log.info("Calculating the best weather.");
        List<LocationMapper> locationsMeetRequirements = getLocationsThatMeetRequirements(locationListFromWeatherApi);
        checkIfTheAreAnyLocations(locationsMeetRequirements);
        log.info("Calculating the best location. Number of locations that meet requirements: {} ", locationsMeetRequirements.size());
        return calculateBestLocation(locationsMeetRequirements);
    }

    private List<LocationMapper> getLocationsThatMeetRequirements(List<LocationDTO> locationListFromWeatherApi) {
        return locationListFromWeatherApi
                .stream()
                .map(this::buildLocationMapperFromDTO)
                .filter(locationMapper ->
                        locationMapper.getWindSpeed() > minWindSpeedRequirement &&
                                locationMapper.getWindSpeed() < maxWindSpeedRequirement)
                .filter(locationMapper ->
                        locationMapper.getTemperature() > minTempSpeedRequirement &&
                                locationMapper.getTemperature() < maxTempSpeedRequirement)
                .toList();
    }

    private void checkIfTheAreAnyLocations(List<LocationMapper> locationsMeetRequirements) {
        if (locationsMeetRequirements.isEmpty()) {
            String message = "Non of the locations meets the requirements.";
            log.info(message);
            throw new NoneLocationMeetsRequirementsException(message);
        }
    }

    private LocationMapper buildLocationMapperFromDTO(LocationDTO locationDTO) {
        return LocationMapper.builder()
                .cityName(locationDTO.getCityName())
                .date(locationDTO.getValidDate())
                .temperature(Float.parseFloat(locationDTO.getTemp()))
                .windSpeed(Float.parseFloat(locationDTO.getWindSpeed()))
                .build();
    }

    LocationMapper calculateBestLocation(List<LocationMapper> locationsList) {
        Map<Float, LocationMapper> map = new LinkedHashMap<>();
        //How to find best weather: wind * 3 + temp.
        locationsList.forEach(location ->
                map.put((location.getWindSpeed() * 3 + location.getTemperature()), location));

        LocationMapper locationResult = map.entrySet()
                .stream()
                .max(Map.Entry.comparingByKey())
                .stream()
                .findFirst()
                .map(Map.Entry::getValue)
                .get();
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
