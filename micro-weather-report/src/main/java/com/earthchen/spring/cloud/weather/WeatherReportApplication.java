package com.earthchen.spring.cloud.weather;


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
import org.springframework.boot.web.servlet.ServletComponentScan;
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


@SpringBootApplication
@ServletComponentScan(basePackages = "com.earthchen.spring.cloud.weather.filter")
@MapperScan("com.earthchen.spring.cloud.weather.mapper")
@EnableScheduling
public class WeatherReportApplication {

    @Autowired
    private CityDataService cityDataService;
    public static void main(String[] args) throws IOException {
        SpringApplication.run(WeatherReportApplication.class, args);

    }
    public void writeCity() throws Exception{

        List<City> cityList = new ArrayList<City>();
        // 读文件
        String path = this.getClass().getClassLoader().getResource("./cityList.csv").getPath();
        InputStreamReader fr = new InputStreamReader(new FileInputStream(path),"GBK");
        BufferedReader br1 = new BufferedReader(fr);

        String str = null;
        while((str = br1.readLine()) != null) {
            String data[] = str.split(",");
            String cityName = null;
            City c = new City();
            c.setCityid(Integer.valueOf(data[0]));
            if(data[5].equals("-")){
                cityName=data[2];
            }else{
                cityName=data[3];
            }
            c.setCityname(cityName);
            c.setCitycode(getPinyin(cityName));
            c.setProvince(data[3]);
            cityList.add(c);
        }
// 关闭流
        br1.close();
        fr.close();
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
            String cityId = city.getCityid().toString();
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
                if(Character.toString(ti).matches("[\\u4e00-\\u9fa5]")){ //匹配是否是中文
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
}
