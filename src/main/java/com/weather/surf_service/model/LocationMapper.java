package com.weather.surf_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
//@Data + mylaca nazwa (dodaj chociaz DTO) LocationMapper kojarzy sie z komponentem odpowiedzialnym za mapowanie pomiedzy typami Location i innymi (warto poczytac o mapstruct)
public class LocationMapper {
    private String cityName;
    private String date;
    private float temperature;
    private float windSpeed;
}
