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
        //TODO tutaj już nie sprawdzam czy data jest ok bo mi wiersz powyżej w razie czego rzuca wyjątek
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
//TODO czy sprawdzać poprawnośc daty?
        //co jeśli metoda jest pruwatna i chcę ją przetestowac w testach? -> test2
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
            //TODO dodać loggery przy rzucaniu wyjątków -> cała apka
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

    //Filter Locations by date.
    private LocationDTO removeUnnecessaryData(Forecast forecast, String date) {
        var locationDTO = forecast.getLocationDTOList()
                .stream()
                .filter(location -> location.getValidDate().equals(date))
                .findFirst().orElseThrow(RuntimeException::new);//TODO get czy orElseThrow???

        locationDTO.setCityName(forecast.getCityName());
        return locationDTO;
    }
}