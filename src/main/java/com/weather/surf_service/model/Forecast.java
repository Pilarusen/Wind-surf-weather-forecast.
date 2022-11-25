package com.weather.surf_service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Forecast {
    //TODO poprawić city_name itd dodać Jsoon property
    private String cityName;
    private List<LocationDTO> locationDTOList;

    @JsonCreator
    public Forecast(@JsonProperty("city_name") String cityName, @JsonProperty("data") List<LocationDTO> weatherList) {
        this.cityName = cityName;
        this.locationDTOList = weatherList;
    }
}
