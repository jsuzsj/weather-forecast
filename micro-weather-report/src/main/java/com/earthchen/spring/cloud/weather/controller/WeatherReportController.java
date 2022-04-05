package com.earthchen.spring.cloud.weather.controller;

import com.earthchen.spring.cloud.weather.mapper.CityMapper;
import com.earthchen.spring.cloud.weather.service.WeatherDataService;
import com.earthchen.spring.cloud.weather.service.*;
import com.earthchen.spring.cloud.weather.vo.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


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

    @GetMapping("/cityId/{cityId}")
    public ModelAndView getReportByCityId(@PathVariable("cityId") String cityId, Model model) throws Exception {
        model.addAttribute("title", "实时的天气预报");
        model.addAttribute("loginUser", getLoginUser());
        model.addAttribute("cityname", "0");
        model.addAttribute("cityid", cityId);
        model.addAttribute("cityList",cityMapper.selectAll());
        model.addAttribute("report", weatherReportService.getDataByCityId(cityId));
        return new ModelAndView("weather/report", "reportModel", model);
    }
    @GetMapping("/cityName/{cityName}")
    public ModelAndView getReportByCityName(@PathVariable("cityName") String cityName, Model model) throws Exception {
        model.addAttribute("title", "实时的天气预报");
        model.addAttribute("cityname", cityName);
        model.addAttribute("loginUser", getLoginUser());
        model.addAttribute("cityid", 0);
        model.addAttribute("cityList", cityDataService.listCity());
        WeatherResponse wr = weatherDataService.getDataByCityName(cityName);
        System.out.println(wr.getData());
        model.addAttribute("report", wr.getData());
        System.out.println(weatherDataService.getDataByCityName(cityName).getData());
        return new ModelAndView("weather/report", "reportModel", model);
    }
}
