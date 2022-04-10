package com.earthchen.spring.cloud.weather.controller;

import com.alibaba.fastjson.JSONObject;
import com.earthchen.spring.cloud.weather.mapper.CityMapper;
import com.earthchen.spring.cloud.weather.mapper.MessageMapper;
import com.earthchen.spring.cloud.weather.service.CityDataService;
import com.earthchen.spring.cloud.weather.util.R;
import com.earthchen.spring.cloud.weather.vo2.City;
import com.earthchen.spring.cloud.weather.vo2.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController extends BaseController{

    @Autowired
    private CityDataService cityDataService;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/send")
    public R sendComment(String sid,String content,String pid) {
        Message message = new Message();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", content);
        message.setUid(getLoginUserId());
        message.setThirdmessageid(sid);
        message.setJdata(jsonObject.toString());
        if(pid==null) {
            message.setMessagetype("normal");
        }else{
            message.setMessagetype("forward");
            message.setForwardmessageid(pid);
        }
        messageMapper.insertComment(message);
        return R.succeed("发送消息成功！");
    }
    @PostMapping("/praise")
    public R praise(Message message) {
        String key = message.getMessageid()+"_praise_"+getLoginUserId();
        if(stringRedisTemplate.hasKey(key)){
            return R.failed("你已经点过赞了！");
        }
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(key,"1");
        message.setPraisecount(message.getPraisecount()+1);
        messageMapper.praise(message);
        return R.succeed("点赞成功！");
    }
}
