package com.weather.surf_service.service;

import com.weather.surf_service.exception.DateFromPastException;
import com.weather.surf_service.exception.DateToFarAwayException;
import com.weather.surf_service.exception.WrongDateFormatException;
import com.weather.surf_service.model.Forecast;
import com.weather.surf_service.model.Location;
import com.weather.surf_service.model.LocationDTO;
import com.weather.surf_service.webclient.weather.WeatherClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherBitService {
    //TODO move date pattern to yml
    private final static String DATE_PATTERN = "yyyy-MM-dd";

    private final WeatherClient weatherClient;

    //WeatherBitService is to provide list of LocationDTO objects.
    //Get Json by WeatherClient, create list of LocationDTO.
    public List<LocationDTO> getWeatherForLocations(String date) {
        log.info("Get Locations for date: {}.", date);
        long dateRange = calculateDateRangeFromToday(date); //if date is from past or more than 15 days from now -> throw exception
        List<Forecast> forecastList;
        try {
            forecastList = Location.locations.entrySet()
                    .stream()
                    .map(
                            element -> weatherClient.
                                    getWeatherForCityCoOrdinates(
                                            element.getKey(),
                                            element.getValue(),
                                            (dateRange + 2)
                                    ))
                    .toList();
        } catch (HttpClientErrorException exception) {
            log.error(exception.getMessage());
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
            //TODO test when api not working, maybe change exception
        }
        return getLocationsForDate(forecastList, date);
    }

    //Map list of Forecasts to list of LocationDTO.
    private List<LocationDTO> getLocationsForDate(List<Forecast> forecastList, String date) {
        return forecastList
                .stream()
                .map(forecast -> removeUnnecessaryData(forecast, date))
                .toList();
    }

    long calculateDateRangeFromToday(String dateString) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        LocalDate date;
        try {
            date = LocalDate.parse(dateString, dateTimeFormatter);
        } catch (DateTimeParseException exception) {
            String message = String.format("Date: %s is not formatted properly.", dateString);
            log.error(message);
            throw new WrongDateFormatException(message);
        }
        long dateRange = ChronoUnit.DAYS.between(LocalDate.now(), date);
        log.info("Date range from now: {}", dateRange);
        if (dateRange < 0) {
            String message = "Provided date is from past. Should be at least today";
            log.error(message);
            throw new DateFromPastException(message);
        }
        if (dateRange > 15) {
            String message = "Provided date is out of range.";
            log.error(message);
            throw new DateToFarAwayException(message);
        }
        return dateRange;
    }

    //Filter Locations by date.
    LocationDTO removeUnnecessaryData(Forecast forecast, String date) {
        var locationDTO = forecast.getLocationDTOList()
                .stream()
                .filter(location -> location.getValidDate().equals(date))
                .findFirst().get();
        //TODO find way how to end stream, here only changes on model could provide exceptions

        locationDTO.setCityName(forecast.getCityName());
        return locationDTO;
    }
}