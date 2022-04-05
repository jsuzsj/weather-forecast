package com.earthchen.spring.cloud.weather.controller;

import com.earthchen.spring.cloud.weather.vo2.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class BaseController {

    /**
     * 获取当前登录的user
     */
    public User getLoginUser() {
        Subject subject = SecurityUtils.getSubject();
        if (subject != null) {
            Object object = subject.getPrincipal();
            if (object != null) {
                return (User) object;
            }
        }
        return null;
    }

    /**
     * 获取当前登录的userId
     */
    public Long getLoginUserId() {
        User loginUser = getLoginUser();
        return loginUser == null ? null : loginUser.getId();
    }
}
