package com.weather.surf_service.controller;


import com.weather.surf_service.model.Forecast;
import com.weather.surf_service.model.LocationDTO;
import com.weather.surf_service.service.WeatherService;
import com.weather.surf_service.webclient.weather.WeatherClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class TestClass {

    @Autowired
    private WebApplicationContext webApplicationContext;


    private MockMvc mockMvc;

    @Mock
    WeatherClient weatherClient;

    @Mock
    WeatherService weatherService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

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
        when(weatherClient.getWeatherForCityCoordinates(locationName, coordinates, 2)).thenReturn(mockForecast);
        //then
        mockMvc.perform(get(url)).andExpect(status().is2xxSuccessful());
    }
}

