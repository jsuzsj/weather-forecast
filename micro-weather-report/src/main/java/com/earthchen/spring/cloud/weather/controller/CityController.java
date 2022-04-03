package com.earthchen.spring.cloud.weather.controller;

import com.earthchen.spring.cloud.weather.mapper.CityMapper;
import com.earthchen.spring.cloud.weather.service.WeatherDataService;
import com.earthchen.spring.cloud.weather.vo.WeatherResponse;
import com.earthchen.spring.cloud.weather.vo2.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/city")
public class CityController {

    @Autowired
    private CityMapper cityMapper;

    @GetMapping("/cityList")
    public List<String> getWeatherByCityId(@RequestParam("provincename") String province) {
        return cityMapper.selectCity(province);
    }
    @GetMapping("/provinceList")
    public List<City> getProvinceList() {
        return cityMapper.selectProvince();
    }
    @GetMapping("/countyList")
    public List<City> getCountyList(@RequestParam("cityname") String cityname) {
        return cityMapper.selectCounty(cityname);
    }
}
