package com.weather.surf_service.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
//@Data z lomboka posiada to wszystko co masz wyzej
public class Forecast {
    private String cityName;
    private List<LocationDTO> locationDTOList;

    @JsonCreator
    public Forecast(@JsonProperty("city_name") String cityName, @JsonProperty("data") List<LocationDTO> weatherList) {
        this.cityName = cityName;
        this.locationDTOList = weatherList;
    }
}
