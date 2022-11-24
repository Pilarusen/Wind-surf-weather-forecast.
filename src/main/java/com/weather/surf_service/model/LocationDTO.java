package com.weather.surf_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class LocationDTO {
    private String city_name;
    private String valid_date;
    private String temp;
    private String wind_spd;
}
