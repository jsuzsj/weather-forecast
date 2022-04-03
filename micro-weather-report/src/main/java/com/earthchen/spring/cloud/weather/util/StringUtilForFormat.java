package com.earthchen.spring.cloud.weather.util;

public class StringUtilForFormat {
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
