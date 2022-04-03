package com.earthchen.spring.cloud.weather.controller;

import com.earthchen.spring.cloud.weather.util.JwtUtil;
import com.earthchen.spring.cloud.weather.vo2.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录Controller
 */

@RestController

public class LoginController {
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    //模拟数据库
    static Map<Integer, User> userMap = new HashMap<>();
    static {
        User user1 = new User();
        User user2 = new User();
        user1.setNickname("5");
        user1.setMobile("123456789");
        user1.setId(1L);
        userMap.put(1,user1);
        user2.setNickname("4");
        user2.setMobile("123456789");
        user2.setId(2L);
        userMap.put(2, user2);
    }

    /**
     * 模拟用户登录
     */

    @RequestMapping("/login")
    public String login(User user){
        for (User dbUser : userMap.values()) {
            if (dbUser.getMobile().equals(user.getMobile()) && dbUser.getNickname().equals(user.getNickname())) {
                logger.info("登录成功！生成token！");
                String token = JwtUtil.createToken(dbUser);
                return token;
            }
        }
        return "";
    }
}
