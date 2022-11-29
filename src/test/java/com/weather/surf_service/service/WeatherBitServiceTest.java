package com.weather.surf_service.service;


import com.weather.surf_service.exception.DateFromPastException;
import com.weather.surf_service.exception.DateTooFarAwayException;
import com.weather.surf_service.exception.WrongDateFormatException;
import com.weather.surf_service.model.Forecast;
import com.weather.surf_service.model.LocationDTO;
import com.weather.surf_service.webclient.weather.WeatherClient;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeatherBitServiceTest {

    WeatherBitService weatherBitService;
    WeatherClient weatherClient;
    LogCaptor logCaptor = LogCaptor.forClass(WeatherBitService.class);

    @BeforeEach
    void setUp() {
        weatherBitService = new WeatherBitService(weatherClient);
    }

    @Test
    public void incorrectDateInputShouldThrowWrongDateFormatException() {
        //given
        String incorrectDateString1 = "yyy";
        String incorrectDateString2 = "";
        String incorrectDateString3 = "2023-11-32";
        //when
        Exception exception1 = assertThrows(WrongDateFormatException.class, () -> weatherBitService.calculateDateRangeFromToday(incorrectDateString1));
        Exception exception2 = assertThrows(WrongDateFormatException.class, () -> weatherBitService.calculateDateRangeFromToday(incorrectDateString2));
        Exception exception3 = assertThrows(WrongDateFormatException.class, () -> weatherBitService.calculateDateRangeFromToday(incorrectDateString3));
        String expectedMessage = "is not formatted properly.";
        List<String> logEvents = logCaptor.getErrorLogs();
        //then
        assertThat(exception1).isInstanceOf(WrongDateFormatException.class).hasMessageContaining(expectedMessage);
        assertThat(exception2).isInstanceOf(WrongDateFormatException.class).hasMessageContaining(expectedMessage);
        assertThat(exception3).isInstanceOf(WrongDateFormatException.class).hasMessageContaining(expectedMessage);
        assertThat(logEvents).hasSize(3);
    }

    @Test
    public void dateFromPastShouldThrowException() {
        //given
        String dateString1 = String.valueOf(LocalDate.now().minusDays(1));
        String dateString2 = String.valueOf(LocalDate.now().minusDays(2));
        String dateString3 = String.valueOf(LocalDate.now().minusDays(30));
        //when
        Exception exception1 = assertThrows(DateFromPastException.class, () -> weatherBitService.calculateDateRangeFromToday(dateString1));
        Exception exception2 = assertThrows(DateFromPastException.class, () -> weatherBitService.calculateDateRangeFromToday(dateString2));
        Exception exception3 = assertThrows(DateFromPastException.class, () -> weatherBitService.calculateDateRangeFromToday(dateString3));
        String expectedMessage = "Provided date is from past. Should be at least today";
        List<String> logEvents = logCaptor.getErrorLogs();
        //then
        assertThat(exception1).isInstanceOf(DateFromPastException.class).hasMessageContaining(expectedMessage);
        assertThat(exception2).isInstanceOf(DateFromPastException.class).hasMessageContaining(expectedMessage);
        assertThat(exception3).isInstanceOf(DateFromPastException.class).hasMessageContaining(expectedMessage);
        assertThat(logEvents).hasSize(3);
    }

    @Test
    public void dateMoreThan16DaysFromNowShouldThrowException() {
        //given
        String dateString1 = String.valueOf(LocalDate.now().plusDays(16));
        String dateString2 = String.valueOf(LocalDate.now().plusDays(17));
        String dateString3 = String.valueOf(LocalDate.now().plusDays(30));
        //when
        Exception exception1 = assertThrows(DateTooFarAwayException.class, () -> weatherBitService.calculateDateRangeFromToday(dateString1));
        Exception exception2 = assertThrows(DateTooFarAwayException.class, () -> weatherBitService.calculateDateRangeFromToday(dateString2));
        Exception exception3 = assertThrows(DateTooFarAwayException.class, () -> weatherBitService.calculateDateRangeFromToday(dateString3));
        String expectedMessage = "Provided date is out of range.";
        List<String> logEvents = logCaptor.getErrorLogs();
        //then
        assertThat(exception1).isInstanceOf(DateTooFarAwayException.class).hasMessageContaining(expectedMessage);
        assertThat(exception2).isInstanceOf(DateTooFarAwayException.class).hasMessageContaining(expectedMessage);
        assertThat(exception3).isInstanceOf(DateTooFarAwayException.class).hasMessageContaining(expectedMessage);
        assertThat(logEvents).hasSize(3);
    }

    @Test
    public void shouldRemoveDataFromForecastCorrectly() {
        //given
        String locationDate = String.valueOf(LocalDate.now().plusDays(2));
        String locationName = "JASTARNIA_POLAND";
        var locationList = List.of(
                new LocationDTO(null, locationDate, "6", "6"),
                new LocationDTO(null, locationDate, "6", "9"));
        Forecast forecast = new Forecast(locationName, locationList);
        //when
        var result = weatherBitService.removeUnnecessaryData(forecast, locationDate);
        //then
        assertThat(result).isInstanceOf(LocationDTO.class);
        assertEquals(locationDate, result.getValidDate());
        assertEquals(locationName, result.getCityName());
    }
}