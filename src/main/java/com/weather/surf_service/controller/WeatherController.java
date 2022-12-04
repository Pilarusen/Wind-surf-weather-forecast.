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
//na przyszlosc bedziesz musial robic interfejs WeatherEndpoint a WeatherController bedzie implementowal ten interfejs, w endpoicie opisuje sie metody do swaggera co zwracaja itp.
    private final WeatherService weatherService;

    @GetMapping("/best-weather/date={date}")
    //zazwyczaj trzeba oddzielic warstwy serwisu od controllera czyli fajnie jakby controller zwrocil LocationResponse lub cos w tym stylu
    public LocationMapper getBestWeather(@PathVariable String date) {
        log.info("Received request getBestWeather with date {}", date);
        return weatherService.getBestWeather(date);
    }
}
