package com.weather.surf_service.controller;

import com.weather.surf_service.model.Forecast;
import com.weather.surf_service.model.LocationDTO;
import com.weather.surf_service.webclient.weather.WeatherClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    WeatherClient weatherClient;


    @Test
    public void shouldReturnStatus2xx() throws Exception {
        //given
        String locationDate = String.valueOf(LocalDate.now().plusDays(2));
        String locationName = "JASTARNIA_POLAND";
        Map<String, String> coordinates = new HashMap<>(Collections.singletonMap("54.70", "18.67"));
        var locationList = List.of(new LocationDTO(locationName, locationDate, "6", "6"));
        Forecast mockForecast = new Forecast(null, locationList);
        String url = "/best-weather/date=" + locationDate;
        //when
        when(weatherClient.getWeatherForCityCoOrdinates(locationName, coordinates, 2)).thenReturn(mockForecast);
        //then
        mockMvc.perform(get(url))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        //Check if weatherClient is not sending request to API
        verify(weatherClient, never()).getWeatherForCityCoOrdinates(any(), any(), anyLong());

        //TODO tego chyba się nie testuje... EWENTUALNIE TESTY INTEGRACYJNE
        //z tego co czytałem testy integracyjne nie powinny mieć mocków, jednak żeby uzyskac oczekiwany rezultat
        //powinienem zrobić mock(WeatherCielnt) i zwrócoć zmokowane Forecast... bo co jeśli strona nie działa?
        //moja apka wywali błąd a tak naprawdę to nie jej wina ale zewnętrznego API.
        //TODO  TEST INTEGRACYJNY POWIENIENEM ROBIĆ MOKUJĄC WEATHER API CLIENT -> MOJE ZDANIE
    }
}