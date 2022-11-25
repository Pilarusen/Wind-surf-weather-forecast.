package com.weather.surf_service.service;

import com.weather.surf_service.model.Forecast;
import com.weather.surf_service.model.Location;
import com.weather.surf_service.model.LocationDTO;
import com.weather.surf_service.webclient.weather.WeatherClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherBitService {
    private final static String DATE_PATTERN = "yyyy-MM-dd";

    private final WeatherClient weatherClient;

    //WeatherBitService is to provide list of LocationDTO objects.

    //Get Json by WeatherClient, create list of LocationDTO.
    public List<LocationDTO> getWeatherForLocations(String date) {
        log.info("Get Locations for date: {}.", date);
        long dateRange = calculateDateRangeFromToday(date); //if date is from past -> throw exception
        var forecastList = Location.locations.entrySet()
                .stream()
                .map(
                        element -> weatherClient.
                        getWeatherForCityCoOrdinates(
                                element.getKey(),
                                element.getValue(),
                                (dateRange + 2)
                                ))
                .toList();
        return getLocationsForDate(forecastList, date);
    }

    //Map list of Forecasts to list of LocationDTO.
    private List<LocationDTO> getLocationsForDate(List<Forecast> forecastList, String date) {
        return forecastList
                .stream()
                .map(forecast -> removeUnnecessaryData(forecast, date))
                .toList();
    }

    private long calculateDateRangeFromToday(String dateString) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        LocalDate date;
        try {
            date = LocalDate.parse(dateString, dateTimeFormatter);
        } catch (DateTimeParseException exception) {
            String message = String.format("Date: %s is not formatted properly.", dateString);
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        long dateRange = ChronoUnit.DAYS.between(LocalDate.now(), date);
        log.info("Date range from now: {}", dateRange);
        if (dateRange < 0) {
            String message = "Provided date is from past. Should be at least today";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        if (dateRange > 15) {
            String message = "Provided date is out of range.";
            log.error(message);
            throw new IllegalArgumentException(message);
        }
        return dateRange;
    }

    //Filter Locations by date.
    private LocationDTO removeUnnecessaryData(Forecast forecast, String date) {
        var locationDTO = forecast.getLocationDTOList()
                .stream()
                .filter(location -> location.getValidDate().equals(date))
                .findFirst().orElseThrow(RuntimeException::new);

        locationDTO.setCityName(forecast.getCityName());
        return locationDTO;
    }
}