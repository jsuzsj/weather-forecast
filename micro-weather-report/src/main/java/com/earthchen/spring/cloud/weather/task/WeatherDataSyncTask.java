package com.earthchen.spring.cloud.weather.task;

import com.earthchen.spring.cloud.weather.config.SessionFactory;
import com.earthchen.spring.cloud.weather.mapper.CityMapper;
import com.earthchen.spring.cloud.weather.service.CityDataService;
import com.earthchen.spring.cloud.weather.service.WeatherDataService;
import com.earthchen.spring.cloud.weather.vo2.City;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 天气数据同步任务
 */
@Service
@Slf4j
public class WeatherDataSyncTask extends BaseTask {


    @Autowired
    private CityDataService cityDataService;

    @Autowired
    private WeatherDataService weatherDataService;

    /**
     * 半小时同步一次
     *
     * @return
     */
    @Override
    public String getCronExpression() {
//        return "* 0/30 * * * ? *";
        return  "0 4 11 * * ? *";
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Weather Data Sync Job. Start！");
        // 获取城市ID列表
        List<City> cityList = null;

        try {
            cityList = cityDataService.listCity();
        } catch (Exception e) {
            log.error("Exception!", e);
        }
        // 遍历城市ID获取天气
        try {
            for (City city : cityList) {
                String cityId = city.getCityid().toString();
                log.info("Weather Data Sync Job, cityId:" + cityId);

                weatherDataService.syncDateByCityId(cityId);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        log.info("Weather Data Sync Job. End！");
    }
}
