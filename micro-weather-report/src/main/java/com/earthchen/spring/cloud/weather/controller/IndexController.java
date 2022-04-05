package com.earthchen.spring.cloud.weather.controller;

import com.earthchen.spring.cloud.weather.mapper.UserMapper;
import com.earthchen.spring.cloud.weather.util.R;
import com.earthchen.spring.cloud.weather.util.StringUtil;
import com.earthchen.spring.cloud.weather.vo2.User;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wf.captcha.ArithmeticCaptcha;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class IndexController extends BaseController {

    @Autowired
    private  UserMapper userService;
    /**
     * 主页
     */
    @GetMapping({"", "/index"})
    public String index(Model model) {
        User user = getLoginUser();
        if (user == null) {
            return "redirect:login";
        }
        // 登录用户信息
        model.addAttribute("loginUser", getLoginUser());
        return "theme/pblog/index";
    }

    /**
     * 错误页
     */
    @RequestMapping("/error/{code}")
    public String error(@PathVariable("code") String code) {
        if ("403".equals(code)) {
            return "error/403";
        } else if ("500".equals(code)) {
            return "error/500";
        }
        return "error/404";
    }


    /**
     * 登录页
     */
    @GetMapping("/login")
    public String login() {
        //若缓存中有用户，则直接跳转到首页
        if (getLoginUser() != null) {
            return "redirect:index";
        }
        return "login";
    }

    @GetMapping("/forget")
    public String forget() {
        return "forget";
    }

    /**
     * 注册页
     */
    @GetMapping("/reg")
    public String reg() {
        return "reg";
    }

    /**
     * 生成验证码 算术类型
     */
    @RequestMapping("/assets/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        captcha.setLen(2);
        captcha.getArithmeticString();
        captcha.text();
        request.getSession().setAttribute("captcha", captcha.text());
        captcha.out(response.getOutputStream());
    }

    /**
     * 登录
     */
    @PostMapping("/login")
    @ResponseBody
    public R login(HttpServletRequest request, String username, String password, String code, boolean rememberMe) {
        if (StringUtil.isBlank(username, password)) {
            return R.failed("账号或密码不能为空");
        }
        if (!MobilesMatches(username)){
            return R.failed("邮箱格式不符");
        }
        String sessionCode = (String) request.getSession().getAttribute("captcha");
        if (code == null || !sessionCode.equals(code.trim().toLowerCase())) {
            return R.failed("验证码不正确");
        }
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
            SecurityUtils.getSubject().login(token);
            return R.succeed("登录成功");
        } catch (UnknownAccountException e) {
            return R.failed("用户不存在");
        } catch (IncorrectCredentialsException e) {
            return R.failed("密码错误");
        } catch (ExcessiveAttemptsException eae) {
            return R.failed("操作频繁，请稍后再试");
        }
    }

    /**
     * 注册
     */
    @PostMapping("/reg")
    @ResponseBody
    public R register(User user, HttpServletRequest request, String code) {
//        String sessionCode = (String) request.getSession().getAttribute("checkCode");
        String sessionCode = "1234";
        if (!MobilesMatches(user.getMobile())){
            return R.failed("邮箱格式不符");
        }
        if (code == null || !sessionCode.equals(code)) {
            return R.failed("验证码不正确");
        }

        String curPin = new Md5Hash(user.getPin(),user.getNickname(),2).toString();
        user.setPin(curPin);
        int result = userService.insert(user);
        if (result==1) {
            return R.succeed("注册成功");
        }
        return R.failed("注册失败");
    }

    /**
     * 找回密码
     */
    @PostMapping("/forgetPwd")
    @ResponseBody
    public R forget(User user, HttpServletRequest request, String code){
        String sessionCode = (String) request.getSession().getAttribute("checkCode");
        if (!MobilesMatches(user.getMobile())){
            return R.failed("手机号格式不正确");
        }
        if (code == null || !sessionCode.equals(code)) {
            return R.failed("验证码不正确");
        }
        //修改密码
        if (userService.updateByPrimaryKey(user)==1){
            //通过邮箱查找对应用户进行更新
            return R.succeed("修改成功");
        }else{
            return R.succeed("修改失败，异常!");
        }
    }

    private boolean MobilesMatches(String email){
        String check = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";

        Pattern regex = Pattern.compile(check);

        Matcher matcher = regex.matcher(email);//匹配邮箱格式

        boolean isMatched = matcher.matches();
        return isMatched;
    }
}
