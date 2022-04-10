package com.earthchen.spring.cloud.weather.util;

import com.earthchen.spring.cloud.weather.vo.Weather;
import com.earthchen.spring.cloud.weather.vo.Yeaterday;
import com.earthchen.spring.cloud.weather.vo2.Forecast;

public class StringUtilForFormat {
    public static Weather transferWeather(com.earthchen.spring.cloud.weather.vo2.Weather weather){
        Weather newWea = new Weather();
        newWea.setGanmao(weather.getGanmao());
        newWea.setWendu(String.valueOf(weather.getWendu()));
        newWea.setCity(String.valueOf(weather.getId()));
        return newWea;
    }
    public static Yeaterday transferYesterday(Forecast f){
        Yeaterday y = new Yeaterday();
        y.setDate(f.getDate());
        y.setHigh(String.valueOf(f.getHigh())+"度");
        y.setLow(String.valueOf(f.getLow())+"度");
        y.setType(f.getType());
        y.setFl(f.getFengli()+"级");
        y.setFx(f.getFengxiang());
        return y;
    }
    public static com.earthchen.spring.cloud.weather.vo.Forecast transferForecast(Forecast f){
        com.earthchen.spring.cloud.weather.vo.Forecast newf = new com.earthchen.spring.cloud.weather.vo.Forecast();
        newf.setFengli(String.valueOf(f.getFengli()));
        newf.setDate(f.getDate());
        newf.setFengxiang("风向是："+f.getFengxiang());
        newf.setHigh(String.valueOf(f.getHigh()));
        newf.setLow(String.valueOf(f.getLow()));
        newf.setType("天气"+f.getType());
        return newf;
    }
    public static Forecast transferForecast(com.earthchen.spring.cloud.weather.vo.Forecast f,String key){
        Forecast newf = new Forecast();
        newf.setFengli(StringUtilForFormat.getNumber(f.getFengli()));
        newf.setDate(f.getDate());
        newf.setFengxiang(f.getFengxiang());
        newf.setHigh(StringUtilForFormat.getNumber(f.getHigh()));
        newf.setLow(StringUtilForFormat.getNumber(f.getLow()));
        newf.setType(f.getType());
        newf.setCityid(Integer.valueOf(key));
        return newf;
    }
    public static Integer getNumber(String str){
        StringBuffer sb = new StringBuffer(str);
        while(sb.length()!=0){
            char ch = sb.charAt(0);
            if(ch>='0'&&ch<='9'){
                break;
            }
            sb.delete(0,1);
        }
        while(sb.length()!=0){
            char ch = sb.charAt(sb.length()-1);
            if(ch>='0'&&ch<='9'){
                break;
            }
            sb.delete(sb.length()-1,sb.length());
        }
        int ans = 0;
        try{
            ans = Integer.valueOf(sb.toString());
        }catch (Exception e){

        }
        return ans;
    }
}
