package com.earthchen.spring.cloud.weather.mapper;

import com.earthchen.spring.cloud.weather.vo2.Weather;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weather
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weather
     *
     * @mbg.generated
     */
    int insert(Weather record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weather
     *
     * @mbg.generated
     */
    Weather selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weather
     *
     * @mbg.generated
     */
    List<Weather> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weather
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Weather record);
}
