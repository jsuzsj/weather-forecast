package com.earthchen.spring.cloud.weather.vo;

import com.earthchen.spring.cloud.weather.service.CityDataService;
import com.earthchen.spring.cloud.weather.service.impl.CityDataServiceImpl;
import com.earthchen.spring.cloud.weather.vo2.City;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Data
@XmlRootElement(name = "c")
@XmlAccessorType(XmlAccessType.FIELD)
public class CityList {

    @XmlElement(name = "d")
    private volatile static List<City> cityList;

    public static List<City> getInstance(){
        if(cityList == null){
            synchronized (CityList.class){
                if(cityList== null){
                    CityDataService cs = new CityDataServiceImpl();
                    try {
                        CityList.cityList = cs.listCity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return cityList;
    }
}