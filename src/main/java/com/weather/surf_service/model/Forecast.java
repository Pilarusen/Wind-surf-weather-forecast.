package com.weather.surf_service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class Forecast {
    private String cityName;
    private List<LocationDTO> locationDTOList;

    @JsonCreator
    public Forecast(@JsonProperty("city_name") String cityName, @JsonProperty("data") List<LocationDTO> weatherList) {
        this.cityName = cityName;
        this.locationDTOList = weatherList;
    }
}
