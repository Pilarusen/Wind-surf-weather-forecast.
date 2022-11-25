package com.weather.surf_service.model;

import lombok.*;

@RequiredArgsConstructor
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
