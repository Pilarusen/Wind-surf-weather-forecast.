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

    private long calculateDateRangeFromToday(String dateString) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = null;
        try {
            date = LocalDate.parse(dateString, dateTimeFormatter);
        } catch (DateTimeParseException exception) {
            log.error("Date: {} is not formatted properly.", dateString);
        }
        long dateRange = ChronoUnit.DAYS.between(LocalDate.now(), date);
        log.info("Date range from now: {}", dateRange);
        if (dateRange < 0) {
            throw new IllegalArgumentException("Provided date is from past. Should be at least today");
        }
        if (dateRange > 15) {
            throw new IllegalArgumentException("Provided date is out of range.");
        }
        return dateRange;
    }

    //Map list of Forecasts to list of LocationDTO.
    private List<LocationDTO> getLocationsForDate(List<Forecast> forecastList, String date) {
        return forecastList
                .stream()
                .map(forecast -> removeUnnecessaryData(forecast, date))
                .toList();

    }

    //TODO test orElseThrow
    //Filter Locations by date.
    private LocationDTO removeUnnecessaryData(Forecast forecast, String date) {
        var locationDTO = forecast.getLocationDTOList()
                .stream()
                .filter(location -> location.getValid_date().equals(date))
                .findFirst().orElseThrow();

        locationDTO.setCity_name(forecast.getCity_name());
        return locationDTO;
    }
}