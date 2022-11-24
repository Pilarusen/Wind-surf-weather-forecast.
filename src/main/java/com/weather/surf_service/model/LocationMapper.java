package com.weather.surf_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationMapper {
    private String city_name;
    private String date;
    private float temperature;
    private float wind_speed;
}
