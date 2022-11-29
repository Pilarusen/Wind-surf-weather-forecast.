package com.weather.surf_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class LocationMapper {
    private String cityName;
    private String date;
    private float temperature;
    private float windSpeed;
}
