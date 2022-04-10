package com.earthchen.spring.cloud.weather.controller;

import com.earthchen.spring.cloud.weather.mapper.CityMapper;
import com.earthchen.spring.cloud.weather.mapper.MessageMapper;
import com.earthchen.spring.cloud.weather.service.WeatherDataService;
import com.earthchen.spring.cloud.weather.service.*;
import com.earthchen.spring.cloud.weather.vo.WeatherResponse;
import com.earthchen.spring.cloud.weather.vo2.City;
import com.earthchen.spring.cloud.weather.vo2.DetailMessage;
import com.earthchen.spring.cloud.weather.vo2.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


/**
 * 天气预报Controller
 */
@RestController
@RequestMapping("/secure/report")
public class WeatherReportController extends  BaseController{

    @Autowired
    private WeatherDataService weatherDataService;
    @Autowired
    private WeatherReportService weatherReportService;

    @Autowired
    private CityDataService cityDataService;

    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private MessageMapper messageMapper;

    @GetMapping("/cityId/{cityId}")
    public ModelAndView getReportByCityId(@PathVariable("cityId") String cityId, Model model) throws Exception {
        String cityName = null;
        String city = null;
        List<City> cityList = cityDataService.listCity();
        for(City c : cityList){
            if(c.getCityid().toString().equals(cityId)){
                cityName = c.getCitycode();
                city = c.getCounty();
                if(city.equals("城区")){
                    city = c.getCityname();
                }
                break;
            }
        }
        if(cityName == null){
            throw new RuntimeException("这个cityId不存在");
        }
        model.addAttribute("title", "实时的天气预报");
        model.addAttribute("loginUser", getLoginUser());
        model.addAttribute("city",city);
        model.addAttribute("cityname", cityName);
        model.addAttribute("cityid", cityId);
        model.addAttribute("cityList",cityMapper.selectAll());
        model.addAttribute("report", weatherReportService.getDataByCityId(cityId));
        return new ModelAndView("weather/report", "reportModel", model);
    }
    @GetMapping("/cityName/{cityName}")
    public ModelAndView getReportByCityName(@PathVariable("cityName") String cityId, Model model) throws Exception {
        String cityName = null;
        String city = null;
        List<City> cityList = cityDataService.listCity();
        for(City c : cityList){
            if(c.getCityid().toString().equals(cityId)){
                cityName = c.getCitycode();
                city = c.getCounty();
                if(city.equals("城区")){
                    city = c.getCityname();
                }
                break;
            }
        }
        if(cityName == null){
            throw new RuntimeException("这个cityId不存在");
        }
        model.addAttribute("title", "实时的天气预报");
        model.addAttribute("loginUser", getLoginUser());
        model.addAttribute("city",city);
        model.addAttribute("cityname", cityName);
        model.addAttribute("cityid", cityId);
        model.addAttribute("cityList",cityMapper.selectAll());
        model.addAttribute("report", weatherReportService.getDataByCityId(cityId));
        return new ModelAndView("weather/report", "reportModel", model);
    }

    @GetMapping("/comment/{cityName}")
    public ModelAndView getComment(@PathVariable("cityName") String cityId, Model model) throws Exception {
        String cityName = null;
        String city = null;
        List<City> cityList = cityDataService.listCity();
        for(City c : cityList){
            if(c.getCityid().toString().equals(cityId)){
                cityName = c.getCitycode();
                city = c.getCounty();
                if(city.equals("城区")){
                    city = c.getCityname();
                }
                break;
            }
        }
        if(cityName == null){
            throw new RuntimeException("这个cityId不存在");
        }
        model.addAttribute("title", "实时的天气预报");
        model.addAttribute("loginUser", getLoginUser());
        model.addAttribute("city",city);
        model.addAttribute("cityname", cityName);
        model.addAttribute("cityid", cityId);
        model.addAttribute("cityList",cityMapper.selectAll());
        model.addAttribute("report", weatherReportService.getDataByCityId(cityId));
        List<DetailMessage> messages = messageMapper.selectByCityId(cityId);
        for (DetailMessage m:messages){
            if(m.getForwardmessageid()!=null){
                Long aLong = Long.valueOf(m.getForwardmessageid());
                m.setParent(messageMapper.selectByPrimaryKey(aLong));
            }
        }
        model.addAttribute("commentList", messages);
        return new ModelAndView("weather/comment", "reportModel", model);
    }
    @GetMapping("/commentById/{cityId}")
    public ModelAndView getCommentById(@PathVariable("cityId") String cityId, Model model) throws Exception {
        model.addAttribute("title", "实时的天气预报");
        model.addAttribute("loginUser", getLoginUser());
        model.addAttribute("cityname", "0");
        model.addAttribute("cityid", cityId);
        model.addAttribute("cityList",cityMapper.selectAll());
        List<DetailMessage> messages = messageMapper.selectByCityId(cityId);
        model.addAttribute("commentList", messages);
        model.addAttribute("report", weatherReportService.getDataByCityId(cityId));
        return new ModelAndView("weather/comment", "reportModel", model);
    }

    @GetMapping("/expert/{cityName}")
    public ModelAndView getExpertSay(@PathVariable("cityName") String cityId, Model model) throws Exception {
        String cityName = null;
        String city = null;
        List<City> cityList = cityDataService.listCity();
        for(City c : cityList){
            if(c.getCityid().toString().equals(cityId)){
                cityName = c.getCitycode();
                city = c.getCounty();
                if(city.equals("城区")){
                    city = c.getCityname();
                }
                break;
            }
        }
        if(cityName == null){
            throw new RuntimeException("这个cityId不存在");
        }
        model.addAttribute("title", "实时的天气预报");
        model.addAttribute("loginUser", getLoginUser());
        model.addAttribute("city",city);
        model.addAttribute("cityname", cityName);
        model.addAttribute("cityid", cityId);
        model.addAttribute("cityList",cityMapper.selectAll());
        model.addAttribute("report", weatherReportService.getDataByCityId(cityId));
        return new ModelAndView("weather/expert", "reportModel", model);
    }
}
