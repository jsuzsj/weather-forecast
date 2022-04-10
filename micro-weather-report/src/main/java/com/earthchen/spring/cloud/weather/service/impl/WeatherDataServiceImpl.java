package com.earthchen.spring.cloud.weather.service.impl;

import com.earthchen.spring.cloud.weather.mapper.ForecastMapper;
import com.earthchen.spring.cloud.weather.mapper.WeatherMapper;
import com.earthchen.spring.cloud.weather.service.WeatherDataService;
import com.earthchen.spring.cloud.weather.util.HttpClientUtil;
import com.earthchen.spring.cloud.weather.util.StringUtilForFormat;
import com.earthchen.spring.cloud.weather.vo.Forecast;
import com.earthchen.spring.cloud.weather.vo.Weather;
import com.earthchen.spring.cloud.weather.vo.WeatherResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.earthchen.spring.cloud.weather.vo2.City;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Slf4j
public class WeatherDataServiceImpl implements WeatherDataService {

    /**
     * 第三方api
     */
    private static final String WEATHER_URI = "http://wthrcdn.etouch.cn/weather_mini?";

    /**
     * 过期时间
     */
    private static final long TIME_OUT = 0L; // 1800s

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CityDataServiceImpl cityDataService;
    @Autowired
    private WeatherMapper weatherMapper;
    @Autowired
    private ForecastMapper forecastMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public WeatherResponse getDataByCityId(String cityId) {
        int flag = 0;
        List<City> cityList = cityDataService.listCity();
        for(City c : cityList){
            if(c.getCityid().toString().equals(cityId)){
                flag = 1;
                break;
            }
        }
        if(flag == 0){
            throw new RuntimeException("这个cityId不存在");
        }
        String uri = WEATHER_URI + "citykey=" + cityId;
        String key = cityId;
        return doGetWeather(uri,key);
    }
    @Override
    public WeatherResponse getDataByCityId2(String cityId) {
        int flag = 0;
        String cityname = null;
        List<City> cityList = cityDataService.listCity();
        for(City c : cityList){
            if(c.getCityid().toString().equals(cityId)){
                cityname = c.getCounty();
                flag = 1;
                break;
            }
        }
        if(flag == 0){
            throw new RuntimeException("这个cityId不存在");
        }
        WeatherResponse ans = new WeatherResponse();
        List<Forecast> forecasts = new ArrayList<>();
        for(com.earthchen.spring.cloud.weather.vo2.Forecast f:forecastMapper.selectSevenDayForecast(Integer.valueOf(cityId))){
            forecasts.add(StringUtilForFormat.transferForecast(f));
        }
        Weather data = new Weather();
        data.setForecast(forecasts);
        data.setYesterday(StringUtilForFormat.transferYesterday(forecastMapper.selectYesterday(Integer.valueOf(cityId))));
        data.setCity(cityname);
        Weather weather = StringUtilForFormat.transferWeather(weatherMapper.getFreshWeather(Integer.valueOf(cityId)));
        data.setWendu(weather.getWendu());
        data.setGanmao(weather.getGanmao());
        ans.setData(data);
        return ans;
    }

    @Override
    public WeatherResponse getDataByCityName(String cityName) {
        String cityId = null;
        List<City> cityList = cityDataService.listCity();
        for(City c : cityList){
            if(c.getCitycode().equals(cityName)){
                cityId = c.getCityid().toString();
                break;
            }
        }
        if(cityId == null){
            throw new RuntimeException("这个cityName不存在");
        }
        String uri = WEATHER_URI + "citykey=" + cityId;
        String key = cityId;
        return doGetWeather(uri,key);
    }

    @Override
    public void syncDateByCityId(String cityId) {
        String uri = WEATHER_URI + "citykey=" + cityId;
        this.saveWeatherData(uri,cityId);
    }

    /**
     * 把天气数据放在缓存
     *
     * @param uri
     */
    private void saveWeatherData(String uri,String key) {
        String strBody = null;
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();

        // 调用服务接口来获取
//        ResponseEntity<String> respString = restTemplate.getForEntity(uri, String.class);
//
//        if (respString.getStatusCodeValue() == 200) {
//            strBody = respString.getBody();
//        }
        strBody = HttpClientUtil.doGet(uri);

        // 数据写入缓存
        ops.set(key, strBody);
        //写入数据库

        WeatherResponse resp = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            resp = mapper.readValue(strBody, WeatherResponse.class);
        } catch (IOException e) {
            //e.printStackTrace();
            log.error("Error!", e);
        }
        Weather weather = resp.getData();
        try{
            com.earthchen.spring.cloud.weather.vo2.Weather newWeather = new com.earthchen.spring.cloud.weather.vo2.Weather();
            newWeather.setCityid(Integer.valueOf(key));
            newWeather.setGanmao(weather.getGanmao());
            newWeather.setWendu(Integer.valueOf(weather.getWendu()));
            weatherMapper.insert(newWeather);
            for(Forecast f:weather.getForecast()) {
                com.earthchen.spring.cloud.weather.vo2.Forecast newf = StringUtilForFormat.transferForecast(f,key);
                forecastMapper.insert(newf);
            }
        }catch (NullPointerException e){
            log.error("not found key is" + key +"'s weather info");
        }
    }

    private WeatherResponse doGetWeather(String uri,String key) {
        String strBody = null;
        ObjectMapper mapper = new ObjectMapper();
        WeatherResponse resp = null;
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        // 先查缓存，缓存有的取缓存中的数据
        if (stringRedisTemplate.hasKey(key)&&false) {
            log.info("Redis has data");
            strBody = ops.get(key);
        } else {
            log.info("Redis don't has data");

            // 缓存没有，再调用服务接口来获取
//            ResponseEntity<String> respString = restTemplate.getForEntity(uri, String.class);
//            if (respString.getStatusCodeValue() == 200) {
//                strBody = respString.getBody();
//            }
            strBody = HttpClientUtil.doGet(uri);

            log.info(strBody);

            // 数据写入缓存
            ops.set(key, strBody);
        }

        try {
            resp = mapper.readValue(strBody, WeatherResponse.class);
        } catch (IOException e) {
            //e.printStackTrace();
            log.error("Error!", e);
        }

        return resp;
    }

}

