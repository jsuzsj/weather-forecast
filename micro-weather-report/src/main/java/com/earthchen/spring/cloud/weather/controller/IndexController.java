package com.earthchen.spring.cloud.weather.controller;

import com.earthchen.spring.cloud.weather.mapper.CityMapper;
import com.earthchen.spring.cloud.weather.mapper.MessageMapper;
import com.earthchen.spring.cloud.weather.mapper.UserMapper;
import com.earthchen.spring.cloud.weather.service.CityDataService;
import com.earthchen.spring.cloud.weather.service.WeatherDataService;
import com.earthchen.spring.cloud.weather.service.WeatherReportService;
import com.earthchen.spring.cloud.weather.util.R;
import com.earthchen.spring.cloud.weather.util.StringUtil;
import com.earthchen.spring.cloud.weather.vo2.City;
import com.earthchen.spring.cloud.weather.vo2.DetailMessage;
import com.earthchen.spring.cloud.weather.vo2.User;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.wf.captcha.ArithmeticCaptcha;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class IndexController extends BaseController {

    @Autowired
    private WeatherDataService weatherDataService;
    @Autowired
    private WeatherReportService weatherReportService;

    @Autowired
    private CityDataService cityDataService;

    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private  UserMapper userService;
    /**
     * 主页
     */
    @GetMapping({"/", "/index"})
    public ModelAndView index(@PathVariable(value = "cityname",required = false)String cityName, Model model) throws Exception {
        User user = getLoginUser();
        if (user == null) {
            return new ModelAndView("login", "reportModel", model);
//            return "redirect:login";
        }
        // 登录用户信息
        model.addAttribute("loginUser", getLoginUser());
        String cityId = null;
        String cityN = null;
        String chieseN = null;
        if(cityName==null){
            cityN = "beijing";
            cityId = "101010100";
            chieseN = "北京";
        }else
        if (!Character.isDigit(cityName.charAt(0))) {
            cityN = cityName;
            List<City> cityList = cityDataService.listCity();
            for (City c : cityList) {
                if (c.getCitycode().equals(cityName)) {
                    cityId = c.getCityid().toString();
                    chieseN = c.getCounty();
                    if(chieseN.equals("城区")){
                        chieseN = c.getCityname();
                    }
                    break;
                }
            }
            if (cityId == null) {
                throw new RuntimeException("这个cityName不存在");
            }
        }else{
            List<City> cityList = cityDataService.listCity();
            for (City c : cityList) {
                if (c.getCityid().toString().equals(cityName)) {
                    cityN = c.getCitycode();
                    chieseN = c.getCounty();
                    if(chieseN.equals("城区")){
                        chieseN = c.getCityname();
                    }
                    break;
                }
            }
            if (cityN == null) {
                throw new RuntimeException("这个cityId不存在");
            }
            cityId = cityName;
        }
        model.addAttribute("title", "实时的天气预报");
        model.addAttribute("loginUser", getLoginUser());
        model.addAttribute("cityid", cityId);
        model.addAttribute("cityname", cityN);
        model.addAttribute("city", chieseN);
        model.addAttribute("cityList", cityDataService.listCity());
        model.addAttribute("report",weatherReportService.getDataByCityId(cityId));
        List<DetailMessage> messages = messageMapper.selectByCityId(cityId);
        model.addAttribute("commentList", messages);
        return new ModelAndView("theme/pblog/index", "reportModel", model);
    }

    /**
     * 主页
     */
    @GetMapping( "/more")
    public ModelAndView more(@PathVariable(value = "cityname",required = false)String cityName, Model model) throws Exception {
        User user = getLoginUser();
        if (user == null) {
            return new ModelAndView("login", "reportModel", model);
//            return "redirect:login";
        }
        // 登录用户信息
        model.addAttribute("loginUser", getLoginUser());
        String cityId = null;
        String cityN = null;
        String chieseN = null;
        if(cityName==null){
            cityN = "beijing";
            cityId = "101010100";
            chieseN = "北京";
        }else
        if (!Character.isDigit(cityName.charAt(0))) {
            cityN = cityName;
            List<City> cityList = cityDataService.listCity();
            for (City c : cityList) {
                if (c.getCitycode().equals(cityName)) {
                    cityId = c.getCityid().toString();
                    chieseN = c.getCounty();
                    if(chieseN.equals("城区")){
                        chieseN = c.getCityname();
                    }
                    break;
                }
            }
            if (cityId == null) {
                throw new RuntimeException("这个cityName不存在");
            }
        }else{
            List<City> cityList = cityDataService.listCity();
            for (City c : cityList) {
                if (c.getCityid().toString().equals(cityName)) {
                    cityN = c.getCitycode();
                    chieseN = c.getCounty();
                    if(chieseN.equals("城区")){
                        chieseN = c.getCityname();
                    }
                    break;
                }
            }
            if (cityN == null) {
                throw new RuntimeException("这个cityId不存在");
            }
            cityId = cityName;
        }
        model.addAttribute("title", "实时的天气预报");
        model.addAttribute("loginUser", getLoginUser());
        model.addAttribute("cityid", cityId);
        model.addAttribute("cityname", cityN);
        model.addAttribute("city", chieseN);
        model.addAttribute("cityList", cityDataService.listCity());
        model.addAttribute("report",weatherReportService.getDataByCityId(cityId));
        List<DetailMessage> messages = messageMapper.selectByCityId(cityId);
        model.addAttribute("commentList", messages);
        return new ModelAndView("theme/pblog/more", "reportModel", model);
    }

    /**
     * 主页
     */
    @GetMapping( "/apiindex")
    public ModelAndView apiindex(Model model) throws Exception {
        User user = getLoginUser();
        if (user == null) {
            return new ModelAndView("login", "reportModel", model);
//            return "redirect:login";
        }
        // 登录用户信息
        model.addAttribute("loginUser", getLoginUser());

        model.addAttribute("title", "实时的天气预报");
        model.addAttribute("loginUser", getLoginUser());
        return new ModelAndView("theme/pblog/apiindex", "reportModel", model);
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
