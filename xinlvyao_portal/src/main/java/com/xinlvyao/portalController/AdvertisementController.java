package com.xinlvyao.portalController;

import com.google.gson.Gson;
import com.xinlvyao.pojo.TbContent;
import com.xinlvyao.portalservice.TbContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdvertisementController {
    @Autowired
    private TbContentService tbContentService;
    @Autowired
    private RedisTemplate redisTemplate;
    @RequestMapping("/")
    public String showAd(Model model){
        String bigAdContent = tbContentService.getBigAdContent(102);
        model.addAttribute("ad1",bigAdContent);
        return "index";
    }

    @RequestMapping("/update/ad/redis")
    public void updateAdRedis(@RequestBody String s){
        System.out.println(s);
        Gson gson = new Gson();
        TbContent tbContent = gson.fromJson(s, TbContent.class);
        Long size = redisTemplate.opsForList().leftPush("com.jd.ad", tbContent);
        System.out.println("redis缓存已更新："+size);
    }
}
