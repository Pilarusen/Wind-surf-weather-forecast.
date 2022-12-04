package com.weather.surf_service.webclient.weather;

import com.weather.surf_service.exception.WeatherApiUnavailableException;
import com.weather.surf_service.model.Forecast;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class WeatherClient {

    //apiKey i weatherUrl dodaj do klasy EnvHelper a tutaj zrob sobie zwyklego @Autowired/konstruktor klasy EnvHelper;)
    @Value("${weatherBit.url}")
    private String weatherUrl;
    @Value("${weatherBit.apiKey}")
    private String apiKey;

    //wykorzystuj springa ;)
    private final RestTemplate restTemplate = new RestTemplate();

    public Forecast getWeatherForCityCoordinates(String locationName,
                                                 Map<String,
                                                         String> coordinates,
                                                 long daysRange) {
        //ta operacje zrobilbym juz na poziomie serwisu, niech client ma w sobie jak najmniej dodatkowej logiki
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
            //do nauczenia i18n, wykorzystywane w wiekszosci projektow (nie chcesz pracowac w takich gdzie tego nie uzywaja ;))
            String message = "api.weatherbit.io is not working";
            log.error(message);
            throw new WeatherApiUnavailableException(message);
        }
        return result;
    }
}
