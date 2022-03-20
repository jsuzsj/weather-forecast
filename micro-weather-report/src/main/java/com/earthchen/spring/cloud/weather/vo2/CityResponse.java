package com.earthchen.spring.cloud.weather.vo2;

import lombok.Data;

@Data
public class CityResponse {
    public Integer cityid;

    public String cityname;

    public String citycode;

    public String province;
}
