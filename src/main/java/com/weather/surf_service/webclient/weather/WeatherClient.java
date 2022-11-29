package com.weather.surf_service.webclient.weather;

import com.weather.surf_service.exception.WeatherApiUnavailableException;
import com.weather.surf_service.exception.WrongLocationCoordinateException;
import com.weather.surf_service.model.Forecast;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
public class WeatherClient {

    //TODO create application.yml to insert url and the rest elements.
    public static final String WEATHER_URL = "https://api.weatherbit.io/v2.0/forecast/";
    public static final String API_KEY = "5872bfc95fb64253a0ad76e768aafe34";
    private final RestTemplate restTemplate = new RestTemplate();

    public Forecast getWeatherForCityCoOrdinates(String locationName, Map<String, String> coOrdinates, long daysRange) {
        String lat = String.join("", coOrdinates.keySet());
        String lon = String.join("", coOrdinates.values());
        isCoordinatesCorrect(lat);
        isCoordinatesCorrect(lon);
        log.info("Get weather for: {}, lat={}, lon={}.", locationName, lat, lon);
        Forecast result;
        try {
            result = restTemplate.getForObject(
                    WEATHER_URL + "/daily?lat={lat}&lon={lon}&days={daysRange}&key={API_KEY}",
                    Forecast.class,
                    lat,
                    lon,
                    daysRange,
                    API_KEY
            );
        } catch (WeatherApiUnavailableException exception) {
            String message = "api.weatherbit.io is not working";
            log.error(message);
            throw new WeatherApiUnavailableException(message);
        }
        return result;
    }

    void isCoordinatesCorrect(String coordinates) {
        boolean matches = coordinates.matches("^(-?\\d+(\\.\\d+)?)$");
        if (!matches) {
            String message = String.format("Coordinates for your location is not correct: %s.", coordinates);
            log.error(message);
            throw new WrongLocationCoordinateException(message);
        }
    }
}
