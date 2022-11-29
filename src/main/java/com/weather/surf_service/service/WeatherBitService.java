package com.weather.surf_service.service;

import com.weather.surf_service.exception.DateFromPastException;
import com.weather.surf_service.exception.DateTooFarAwayException;
import com.weather.surf_service.exception.WrongDateFormatException;
import com.weather.surf_service.model.Forecast;
import com.weather.surf_service.model.Location;
import com.weather.surf_service.model.LocationDTO;
import com.weather.surf_service.webclient.weather.WeatherClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.weather.surf_service.util.Utils.DATE_PATTERN;

@Service
@Slf4j
@AllArgsConstructor
public class WeatherBitService {

    private final WeatherClient weatherClient;

    //WeatherBitService is to provide list of LocationDTO objects.
    //Get Json by WeatherClient, create list of LocationDTO.
    public List<LocationDTO> getWeatherForLocations(String date) {
        log.info("Get Locations for date: {}.", date);
        int dateRange = calculateDateRangeFromToday(date); //if date is from past or more than 15 days from now -> throw exception
        List<Forecast> forecastList;
        forecastList = Location.locations.entrySet().stream()
                .map(element -> weatherClient.
                        getWeatherForCityCoordinates(
                                element.getKey(), //lat
                                element.getValue(), //lon
                                (dateRange + 2)))
                .toList();
        return getLocationsForDate(forecastList, date);
    }

    private List<LocationDTO> getLocationsForDate(List<Forecast> forecastList, String date) {
        return forecastList
                .stream()
                .map(forecast -> removeUnnecessaryData(forecast, date))
                .toList();
    }

    int calculateDateRangeFromToday(String dateString) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        LocalDate date;
        try {
            date = LocalDate.parse(dateString, dateTimeFormatter);
        } catch (DateTimeParseException exception) {
            String message = String.format("Date: %s is not formatted properly.", dateString);
            log.error(message);
            throw new WrongDateFormatException(message);
        }
        int dateRange = (int) ChronoUnit.DAYS.between(LocalDate.now(), date);
        log.info("Date range from now: {}", dateRange);
        if (dateRange < 0) {
            String message = "Provided date is from past. Should be at least today";
            log.error(message);
            throw new DateFromPastException(message);
        }
        if (dateRange > 15) {
            String message = "Provided date is out of range.";
            log.error(message);
            throw new DateTooFarAwayException(message);
        }
        return dateRange;
    }

    //Filter Locations by date.
    LocationDTO removeUnnecessaryData(Forecast forecast, String date) {
        LocationDTO locationDTO = forecast.getLocationDTOList()
                .stream()
                .filter(location -> location.getValidDate().equals(date))
                .findFirst().get();
        locationDTO.setCityName(forecast.getCityName());
        return locationDTO;
    }
}