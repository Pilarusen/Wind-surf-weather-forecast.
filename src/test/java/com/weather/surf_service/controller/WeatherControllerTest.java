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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    WeatherClient weatherClient;

    @Test
    public void allParametersOKShouldReturnStatus2xx() throws Exception {
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
    }

    @Test
    public void allParametersOKNoneOfLocationsMeetsRequirementsShouldReturnStatus204() throws Exception {
        //given
        String locationDate = String.valueOf(LocalDate.now().plusDays(2));
        String locationName = "JASTARNIA_POLAND";
        Map<String, String> coordinates = new HashMap<>(Collections.singletonMap("54.70", "18.67"));
        var locationList = List.of(new LocationDTO(locationName, locationDate, "0", "0"));
        Forecast mockForecast = new Forecast(null, locationList);
        String url = "/best-weather/date=" + locationDate;
        //when
        when(weatherClient.getWeatherForCityCoOrdinates(locationName, coordinates, 2)).thenReturn(mockForecast);
        //then
        mockMvc.perform(get(url))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void dateFromPastShouldReturnStatus400() throws Exception {
        //given
        String locationDate = String.valueOf(LocalDate.now().minusDays(2));
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
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void dateToFarAwayShouldReturnStatus400() throws Exception {
        //given
        String locationDate = String.valueOf(LocalDate.now().plusDays(17));
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
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void dateFormatIncorrectShouldReturnStatus400() throws Exception {
        //given
        String locationDate = "Incorrect date format.";
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
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //TODO can not test that because of early checking in class WeatherClient
//    @Test
//    public void wrongLocationCoordinatesShouldReturnStatus500() throws Exception {
//        //given
//        String locationDate = String.valueOf(LocalDate.now().plusDays(2));
//        String locationName = "JASTARNIA_POLAND";
//        Map<String, String> coordinates = new HashMap<>(Collections.singletonMap("wrong", "wrong"));
//        var locationList = List.of(new LocationDTO(locationName, locationDate, "6", "6"));
//        Forecast mockForecast = new Forecast(null, locationList);
//        String url = "/best-weather/date=" + locationDate;
//        //when
//        //then
//        mockMvc.perform(get(url))
//                .andDo(MockMvcResultHandlers.print())
//                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
//    }

    //TODO add test when api not working
}