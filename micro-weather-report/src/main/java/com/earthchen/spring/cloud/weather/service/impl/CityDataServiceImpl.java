package com.earthchen.spring.cloud.weather.service.impl;

import com.earthchen.spring.cloud.weather.mapper.CityMapper;
import com.earthchen.spring.cloud.weather.service.CityDataService;
import com.earthchen.spring.cloud.weather.service.WeatherDataService;
import com.earthchen.spring.cloud.weather.service.WeatherReportService;
import com.earthchen.spring.cloud.weather.vo2.City;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CityDataServiceImpl implements CityDataService {

    @Resource
    CityMapper cityMapper ;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<City> listCity() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ObjectMapper mapper = new ObjectMapper();
        List<City> mp = new ArrayList<>();
        if (stringRedisTemplate.hasKey("cityList")) {
            try {
                String jsonString = ops.get("cityList");
                mp=mapper.readValue(jsonString,new TypeReference<List<City>>(){});
                return mp ;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mp = cityMapper.selectAll();
        try {
            String strjosn = mapper.writeValueAsString(mp);
            ops.set("cityList",strjosn);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return mp;
    }

    @Override
    public List<City> listProvince() throws Exception {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ObjectMapper mapper = new ObjectMapper();
        List<City> mp = new ArrayList<>();
        if (stringRedisTemplate.hasKey("cityProvince")) {
            try {
                String jsonString = ops.get("cityProvince");
                mp=mapper.readValue(jsonString,new TypeReference<List<City>>(){});
                return mp ;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mp = cityMapper.selectProvince();
        try {
            String strjosn = mapper.writeValueAsString(mp);
            ops.set("cityList",strjosn);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return mp;
    }

}
