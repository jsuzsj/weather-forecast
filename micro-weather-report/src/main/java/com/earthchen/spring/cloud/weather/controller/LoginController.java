package com.earthchen.spring.cloud.weather.controller;

import com.alibaba.fastjson.JSONObject;
import com.earthchen.spring.cloud.weather.mapper.UserMapper;
import com.earthchen.spring.cloud.weather.vo2.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录Controller
 */

@Controller
public class LoginController {
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserMapper userMapper;
    /**
     * 登录方法
     * @param userInfo
     * @return
     */
    @RequestMapping(value = "/ajaxLogin", method = RequestMethod.POST)
    @ResponseBody
    public String ajaxLogin(User userInfo) {
        JSONObject jsonObject = new JSONObject();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(userInfo.getMobile(), userInfo.getPin());
        try {
            subject.login(token);
            jsonObject.put("token", subject.getSession().getId());
            jsonObject.put("msg", "登录成功");
        } catch (IncorrectCredentialsException e) {
            jsonObject.put("msg", "密码错误");
        } catch (LockedAccountException e) {
            jsonObject.put("msg", "登录失败，该用户已被冻结");
        } catch (AuthenticationException e) {
            jsonObject.put("msg", "该用户不存在");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    @RequestMapping(value = "/regis", method = RequestMethod.POST)
    @ResponseBody
    public String ajaxRegis(User userInfo) {
        JSONObject jsonObject = new JSONObject();
        if(userMapper.findByMobile(userInfo.getMobile())==null){
            jsonObject.put("msg", "注册失败，该用户已经注册");
            return jsonObject.toString();
        }
        String curPin = new Md5Hash(userInfo.getPin(),userInfo.getNickname(),2).toString();
        userInfo.setPin(curPin);
        userMapper.insert(userInfo);
        jsonObject.put("msg", "注册成功，跳转登录");
        return jsonObject.toString();
    }
    /**
     * 未登录，shiro应重定向到登录界面，此处返回未登录状态信息由前端控制跳转页面
     * @return
     */
    @RequestMapping(value = "/unauth")
    public String unauth() {
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("code", "1000000");
//        map.put("msg", "未登录");
        return "login";
    }
}
