package com.earthchen.spring.cloud.weather.service;

import com.earthchen.spring.cloud.weather.vo.*;

/**
 * Weather Data Service.
 */
public interface WeatherDataService {

    /**
     * 根据城市ID查询天气数据
     *
     * @param cityId
     * @return
     */
    WeatherResponse getDataByCityId(String cityId);

    WeatherResponse getDataByCityId2(String cityId);

    /**
     * 根据城市名称查询天气数据
     *
     * @param cityName
     * @return
     */
    WeatherResponse getDataByCityName(String cityName);


    /**
     * 根据城市ID来同步天气
     *
     * @param cityId
     */
    void syncDateByCityId(String cityId);
}
