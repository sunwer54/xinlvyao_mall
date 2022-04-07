package com.xinlvyao.provider;

import com.xinlvyao.api.ContentServiceApi;
import com.xinlvyao.mapper.TbContentMapper;
import com.xinlvyao.pojo.TbContent;
import com.xinlvyao.pojo.TbContentExample;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 *  提供获取门户轮播广告的远程接口实现
 */

@Service
public class ContentServiceApiImpl implements ContentServiceApi {
    @Autowired
    private TbContentMapper tbContentMapper;
    @Override
    public List<TbContent> selectContentByCatId(long categoryId) {
        //封装查询条件（categoryId）
        TbContentExample exp = new TbContentExample();
        exp.createCriteria().andCategoryIdEqualTo(categoryId);
        //设置排序
        exp.setOrderByClause("created desc");
        return tbContentMapper.selectByExampleWithBLOBs(exp);
    }
}
