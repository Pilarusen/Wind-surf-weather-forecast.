package com.weather.surf_service.controller;

import com.weather.surf_service.model.LocationMapper;
import com.weather.surf_service.service.WeatherService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class WeatherController {
    //todo,  komentarze, non lacatons mmets usuną klase,

    private final WeatherService weatherService;

    @GetMapping("/best-weather/date={date}")
    public LocationMapper getBestWeather(@PathVariable String date) {
        log.info("Received request getBestWeather with date {}", date);
        return weatherService.getBestWeather(date);
    }
}
