package com.earthchen.spring.cloud.weather;


import com.earthchen.spring.cloud.weather.config.SessionFactory;
import com.earthchen.spring.cloud.weather.mapper.CityMapper;
import com.earthchen.spring.cloud.weather.service.WeatherDataService;
import com.earthchen.spring.cloud.weather.service.impl.WeatherDataServiceImpl;
import com.earthchen.spring.cloud.weather.task.WeatherDataSyncTask;
import com.earthchen.spring.cloud.weather.util.HttpClientUtil;
import com.earthchen.spring.cloud.weather.util.StringUtilForFormat;
import com.earthchen.spring.cloud.weather.vo2.City;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.earthchen.spring.cloud.weather.config.SessionFactory;
import com.earthchen.spring.cloud.weather.mapper.CityMapper;
import com.earthchen.spring.cloud.weather.service.CityDataService;
import com.earthchen.spring.cloud.weather.vo2.City;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;



@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RestTemplateTest {

    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    private CityMapper cityMapper;


    @Test
    public void writeCity() throws Exception{

        List<City> cityList = new ArrayList<City>();
        // ���ļ�
        String path = this.getClass().getClassLoader().getResource("./cityList.csv").getPath();
        InputStreamReader fr = new InputStreamReader(new FileInputStream(path),"GBK");
        BufferedReader br1 = new BufferedReader(fr);
        String str = null;
        while((str = br1.readLine()) != null) {
            String data[] = str.split(",");
            City c = new City();
            c.setCityid(Integer.valueOf(data[0]));
            c.setCounty(data[2]);
            c.setCityname(data[3]);
            if(data[5].equals("-")){
                c.setCitycode(getPinyin(data[2]));
            }else{
                c.setCitycode(getPinyin(data[3]));
            }
            c.setProvince(data[4]);
            cityMapper.insert(c);
        }
// �ر���
        br1.close();
        fr.close();
    }

    private String getPinyin(String china){
        HanyuPinyinOutputFormat formart = new HanyuPinyinOutputFormat();
        formart.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        formart.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        formart.setVCharType(HanyuPinyinVCharType.WITH_V);
        char[] arrays = china.trim().toCharArray();
        String result = "";
        try {
            for (int i=0;i<arrays.length;i++) {
                char ti = arrays[i];
                if(Character.toString(ti).matches("[\\u4e00-\\u9fa5]")){ //ƥ���Ƿ�������
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(ti,formart);
                    result += temp[0];
                }else{
                    result += ti;
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }

        return result;
    }
    @Test
    public void getData() {

        String ob = HttpClientUtil.doGet("http://wthrcdn.etouch.cn/weather_mini?citykey=101280101");
//        String ob = restTemplate.getForEntity("http://127.0.0.1:8080/rooms/list", String.class).getBody();
        log.info(ob);

    }
    @Test
    public void test1(){
        final List<City> cities = cityMapper.selectAll();
        System.out.println(cities);
    }

    @Test
    public void test2(){
        List<String> cs = cityMapper.selectCity("湖南");
        List<City> cc = cityMapper.selectCounty("常德");
        System.out.println(cs);
    }
}
