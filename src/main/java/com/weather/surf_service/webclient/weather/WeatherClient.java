package com.weather.surf_service.webclient.weather;

import com.weather.surf_service.exception.WeatherApiUnavailableException;
import com.weather.surf_service.model.Forecast;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Component
public class WeatherClient {

    @Value("${weatherBit.url}")
    private String weatherUrl;
    @Value("${weatherBit.apiKey}")
    private String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();

    public Forecast getWeatherForCityCoordinates(String locationName,
                                                 Map<String,
                                                         String> coordinates,
                                                 long daysRange) {
        String lat = String.join("", coordinates.keySet());
        String lon = String.join("", coordinates.values());
        log.info("Get weather for: {}, lat={}, lon={}.", locationName, lat, lon);
        Forecast result;
        try {
            result = restTemplate.getForObject(
                    weatherUrl + "/daily?lat={lat}&lon={lon}&days={daysRange}&key={API_KEY}",
                    Forecast.class,
                    lat,
                    lon,
                    daysRange,
                    apiKey);
        } catch (RestClientException exception) {
            String message = "api.weatherbit.io is not working";
            log.error(message);
            throw new WeatherApiUnavailableException(message);
        }
        return result;
    }
}
