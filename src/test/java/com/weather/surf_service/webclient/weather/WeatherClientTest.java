package com.weather.surf_service.webclient.weather;


import com.weather.surf_service.exception.WrongLocationCoordinateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class WeatherClientTest {

    WeatherClient weatherClient;

    @BeforeEach
    void setUp() {
        weatherClient = new WeatherClient();
    }

    @Test
    public void incorrectCoordinatesShouldThrowWrongLocationCoordinate() {
        //given
        String coordinates1 = "22.y2";
        String coordinates2 = "22y2";
        String coordinates3 = "22,y2";
        //when
        Exception exception1 = assertThrows(WrongLocationCoordinateException.class, () -> weatherClient.isCoordinatesCorrect(coordinates1));
        Exception exception2 = assertThrows(WrongLocationCoordinateException.class, () -> weatherClient.isCoordinatesCorrect(coordinates2));
        Exception exception3 = assertThrows(WrongLocationCoordinateException.class, () -> weatherClient.isCoordinatesCorrect(coordinates3));
        String expectedMessage = "Coordinates for your location is not correct:";
        //then
        assertThat(exception1.getMessage()).contains(expectedMessage);
        assertThat(exception2.getMessage()).contains(expectedMessage);
        assertThat(exception3.getMessage()).contains(expectedMessage);
    }
}