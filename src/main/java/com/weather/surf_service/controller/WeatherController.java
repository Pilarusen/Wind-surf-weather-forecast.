package com.weather.surf_service.controller;

import com.weather.surf_service.model.LocationDTO;
import com.weather.surf_service.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/best-weather/{date}")
    public LocationDTO getBestWeather(@PathVariable String date) {
        log.info("Received request getBestWeather with date {}", date);
        return weatherService.getBestWeather(date);
    }
}
