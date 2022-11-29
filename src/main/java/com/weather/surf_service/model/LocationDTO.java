package com.weather.surf_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LocationDTO {
    @JsonProperty("city_name")
    private String cityName;
    @JsonProperty("valid_date")
    private String validDate;
    private String temp;
    @JsonProperty("wind_spd")
    private String windSpeed;
}


