package com.earthchen.spring.cloud.weather.service.impl;

import com.earthchen.spring.cloud.weather.service.CityDataService;
import com.earthchen.spring.cloud.weather.util.XmlBuilderUtil;
import com.earthchen.spring.cloud.weather.vo.City;
import com.earthchen.spring.cloud.weather.vo.CityList;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CityDataServiceImpl implements CityDataService {

    @Override
    public List<City> listCity() throws Exception {
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
            c.setCityId(data[0]);
            if(data[5].equals("-")){
                cityName=data[2];
            }else{
                cityName=data[3];
            }
            c.setCityName(cityName);
            c.setCityCode(getPinyin(cityName));
            cityList.add(c);
        }
// 关闭流
        br1.close();
        fr.close();
        return cityList;
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