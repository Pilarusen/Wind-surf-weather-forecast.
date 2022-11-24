package com.weather.surf_service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Forecast {
    private String city_name;
    private List<LocationDTO> locationDTOList;

    @JsonCreator
    public Forecast(@JsonProperty("city_name") String city_name, @JsonProperty("data") List<LocationDTO> weatherList) {
        this.city_name = city_name;
        this.locationDTOList = weatherList;
    }
}
