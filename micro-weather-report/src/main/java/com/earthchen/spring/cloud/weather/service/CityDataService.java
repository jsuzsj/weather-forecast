package com.earthchen.spring.cloud.weather.service;

import com.earthchen.spring.cloud.weather.vo2.City;

import java.util.List;

public interface CityDataService {

    /**
     * 获取City列表
     * @return
     * @throws Exception
     */
    List<City> listCity() throws Exception;


    /**
     * 获取所辖市区的列表
     * @return
     * @throws Exception
     */
    List<City> listProvince() throws Exception;
}
