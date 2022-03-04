package com.earthchen.spring.cloud.weather.controller;

import com.earthchen.spring.cloud.weather.service.CityDataService;
import com.earthchen.spring.cloud.weather.service.WeatherDataService;
import com.earthchen.spring.cloud.weather.service.WeatherReportService;
import com.earthchen.spring.cloud.weather.vo.CityList;
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
@RequestMapping("/report")
public class WeatherReportController {

    @Autowired
    private WeatherDataService weatherDataService;
    @Autowired
    private WeatherReportService weatherReportService;

    @GetMapping("/cityId/{cityId}")
    public ModelAndView getReportByCityId(@PathVariable("cityId") String cityId, Model model) throws Exception {
        model.addAttribute("title", "Test的天气预报");
        model.addAttribute("cityId", cityId);
        model.addAttribute("cityList", CityList.getInstance());
        model.addAttribute("report", weatherReportService.getDataByCityId(cityId));
        return new ModelAndView("weather/report", "reportModel", model);
    }
    @GetMapping("/cityName/{cityName}")
    public ModelAndView getReportByCityName(@PathVariable("cityName") String cityName, Model model) throws Exception {
        model.addAttribute("title", "Test的天气预报");
        model.addAttribute("cityName", cityName);
        model.addAttribute("cityId", 0);
        model.addAttribute("cityList", CityList.getInstance());
        model.addAttribute("report", weatherDataService.getDataByCityName(cityName).getData());
        return new ModelAndView("weather/report", "reportModel", model);
    }
}