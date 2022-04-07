package com.xinlvyao.api;

import com.xinlvyao.pojo.TbContent;

import java.util.List;

/**
 * 提供获取门户轮播广告的远程接口
 */
public interface ContentServiceApi {
    public List<TbContent> selectContentByCatId(long categoryId);
}
