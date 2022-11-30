package com.weather.surf_service.service;

import com.weather.surf_service.exception.NoneLocationMeetsRequirementsException;
import com.weather.surf_service.exception.WrongDateFormatException;
import com.weather.surf_service.model.LocationDTO;
import com.weather.surf_service.model.LocationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WeatherServiceTest {

    @Mock
    private WeatherBitService weatherBitService;
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        weatherService = new WeatherService(weatherBitService);
    }

    @Test
    public void shouldThrowWrongDateFormatException() {
        //given
        String wrongDateFormat1 = "2023-11-32";
        String wrongDateFormat2 = "wrong date";
        //when
        Exception exception1 = assertThrows(WrongDateFormatException.class, () -> weatherService.getBestWeather(wrongDateFormat1));
        Exception exception2 = assertThrows(WrongDateFormatException.class, () -> weatherService.getBestWeather(wrongDateFormat2));
        String expectedMessage = "Incorrect date format:";
        //then
        assertThat(exception1).isInstanceOf(WrongDateFormatException.class)
                .hasMessageContaining(expectedMessage);
        assertThat(exception2).isInstanceOf(WrongDateFormatException.class)
                .hasMessageContaining(expectedMessage);
    }

    @Test
    public void shouldReturnOneLocationWhenTwoMeetRequirements() {
        //given
        String locationDate = String.valueOf(LocalDate.now().plusDays(2));
        String bestLocationName = "bestLocation";
        List<LocationDTO> locationList = List.of(
                new LocationDTO(bestLocationName, locationDate, "8", "6"),
                new LocationDTO("locationName2", locationDate, "6", "6"),
                new LocationDTO("locationName3", locationDate, "4", "4"));
        //when
        var result = weatherService.findBestWeather(locationList);
        //then
        assertEquals(result.getCityName(), bestLocationName);
    }

    @Test
    public void noneOfLocationsMeetRequirementsShouldThrowException() {
        //given
        String locationDate = String.valueOf(LocalDate.now().plusDays(2));
        List<LocationDTO> locationList = List.of(
                new LocationDTO("locationName1", locationDate, "1", "1"),
                new LocationDTO("locationName2", locationDate, "1", "1"),
                new LocationDTO("locationName3", locationDate, "1", "1"));
        //when
        Exception exception = assertThrows(NoneLocationMeetsRequirementsException.class,
                () -> weatherService.findBestWeather(locationList));
        String expectedMessage = "Non of the locations meets the requirements.";
        //then
        assertEquals(exception.getMessage(), expectedMessage);
    }

    @Test
    public void oneLocationMeetsRequirementsShouldReturnOneLocation() {
        //given
        String locationDate = String.valueOf(LocalDate.now().plusDays(2));
        String meetRequirementsLocationName = "bestLocation";
        List<LocationDTO> locationList = List.of(
                new LocationDTO(meetRequirementsLocationName, locationDate, "8", "6"),
                new LocationDTO("locationName2", locationDate, "1", "1"),
                new LocationDTO("locationName3", locationDate, "1", "1"));
        //when
        var result = weatherService.findBestWeather(locationList);
        //then
        assertEquals(result.getCityName(), meetRequirementsLocationName);
    }

    @Test
    public void shouldCorrectlyCountBestLocation() {
        //given
        String locationDate = String.valueOf(LocalDate.now().plusDays(2));
        String bestLocationName = "bestLocation";
        float bestLocationTemp = 6.0F;
        float bestLocationWind = 6.0F;
        List<LocationMapper> locationList = List.of(
                new LocationMapper(bestLocationName, locationDate, bestLocationTemp, bestLocationWind),
                new LocationMapper("locationName2", locationDate, 2.0F, 1.0F),
                new LocationMapper("locationName3", locationDate, 2.0F, 1.0F));
        //when
        var resultLocation = weatherService.calculateBestLocation(locationList);
        float resultCalculationResult = resultLocation.getWindSpeed() * 3 + resultLocation.getTemperature();
        float expectedCalculationResult = bestLocationWind * 3 + bestLocationTemp;
        //then
        assertEquals(resultCalculationResult, expectedCalculationResult);
    }
}