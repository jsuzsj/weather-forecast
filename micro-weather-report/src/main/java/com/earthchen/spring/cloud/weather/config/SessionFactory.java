package com.earthchen.spring.cloud.weather.config;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SessionFactory {
    private static String  resource = "mybatis-config.xml";
    private static volatile InputStream inputStream = null;
    private static SqlSessionFactory sqlSessionFactory;
    public static SqlSession getSession(){
        try {
            if(inputStream == null){
                synchronized (SessionFactory.class){
                    if(inputStream == null){
                        SessionFactory.inputStream = Resources.getResourceAsStream(resource);
                        SessionFactory.sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sqlSessionFactory.openSession();
    }
}
