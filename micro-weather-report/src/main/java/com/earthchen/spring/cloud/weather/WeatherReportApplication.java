package com.earthchen.spring.cloud.weather;


import com.earthchen.spring.cloud.weather.config.SessionFactory;
import com.earthchen.spring.cloud.weather.mapper.CityMapper;
import com.earthchen.spring.cloud.weather.service.CityDataService;
import com.earthchen.spring.cloud.weather.vo2.City;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;


@SpringBootApplication
@EnableScheduling
public class WeatherReportApplication {

    @Autowired
    private CityDataService cityDataService;
    public static void main(String[] args) throws IOException {
        SpringApplication.run(WeatherReportApplication.class, args);
    }
    public void testFunction(){
        // ��ȡ����ID�б�
        List<City> cityList = null;

        try {
            cityList = cityDataService.listCity();
        } catch (Exception e) {
        }
        SqlSession session = SessionFactory.getSession();
        // ��������ID��ȡ����
        for (City city : cityList) {
            session.getMapper(CityMapper.class).insert(city);
            String cityId = city.getId().toString();
        }
    }
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory());
        //��Ϣת�����б�
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        //������Ϣת����StringHttpMessageConverter��������utf-8
        messageConverters.set(1,
                new StringHttpMessageConverter(StandardCharsets.UTF_8));//֧�������ַ�����Ĭ��ISO-8859-1��֧��utf-8

        return restTemplate;
    }
}
