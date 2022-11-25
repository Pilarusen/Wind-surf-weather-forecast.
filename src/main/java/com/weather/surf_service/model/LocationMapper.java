package com.weather.surf_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class LocationMapper {
    private String cityName;
    private String date;
    private float temperature;
    private float windSpeed;
}
