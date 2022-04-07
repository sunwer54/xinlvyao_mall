package com.xinlvyao.portalservice.impl;

import com.google.gson.Gson;
import com.xinlvyao.api.ContentServiceApi;
import com.xinlvyao.commons.BigAd;
import com.xinlvyao.pojo.TbContent;
import com.xinlvyao.portalservice.TbContentService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 提供获取门户轮播广告的本地接口实现
 */
@Service
public class TbContentServiceImpl implements TbContentService {
    @Reference
    private ContentServiceApi contentServiceApi;

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    //@Cacheable(cacheNames = "com.jd.content",key = "'bigAdvertisement'")
    public String getBigAdContent(long categoryId) {
        List<TbContent> tbContents = null;
        //从数据中执行查询前先查找在redis中是否有存储该数据
        List<TbContent> range = redisTemplate.opsForList().range("com.jd.ad", 0, -1);
        if (range.size()!=0&&range!=null){
            //redis中有缓存，则使用缓存
            tbContents = range;
        }else {
            //redis中没有缓存，按categoryId查询出全部对应的内容，并将其放入redis缓存中
            tbContents = contentServiceApi.selectContentByCatId(categoryId);
            Long size = redisTemplate.opsForList().rightPushAll("com.jd.ad", tbContents);
            System.out.println(size);
        }
        //集合封装BigAd对象
        List<BigAd> list = new ArrayList<>();
        //遍历全部内容，对数据进行封装
        for (TbContent tbContent:tbContents){
            //数据封装对象
            BigAd bigAd = new BigAd();
            bigAd.setHeight(240);
            bigAd.setHeightB(240);
            bigAd.setWidth(670);
            bigAd.setWidthB(550);
            bigAd.setSrc(tbContent.getPic());
            bigAd.setSrcB(tbContent.getPic2());
            bigAd.setHref(tbContent.getUrl());
            bigAd.setAlt("");
            list.add(bigAd);
        }
        //将list集合转换成前台需要的json格式字符串
        Gson gson = new Gson();
        String json = gson.toJson(list);
        return json;
    }
}
