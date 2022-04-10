package com.earthchen.spring.cloud.weather.mapper;

import com.earthchen.spring.cloud.weather.vo2.Forecast;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ForecastMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table forecast
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table forecast
     *
     * @mbg.generated
     */
    int insert(Forecast record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table forecast
     *
     * @mbg.generated
     */
    Forecast selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table forecast
     *
     * @mbg.generated
     */
    List<Forecast> selectAll();
    List<Forecast> selectSevenDayForecast(Integer cityid);
    Forecast selectYesterday(Integer cityid);
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table forecast
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Forecast record);
}
