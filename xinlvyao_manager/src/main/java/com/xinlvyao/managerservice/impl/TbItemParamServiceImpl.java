package com.xinlvyao.managerservice.impl;

import com.xinlvyao.api.TbItemParamServiceApi;
import com.xinlvyao.pojo.TbItemParam;
import com.xinlvyao.managerservice.TbItemParamService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * 提供后台商品规格参数中的各个操作（全部商品规格参数，添加商品规格参数，
 * 删除商品规格参数）的TbItemParamService本地接口实现
 */
@Service//交给Spring容器管理
public class TbItemParamServiceImpl implements TbItemParamService {

    //订阅远程服务
    @Reference
    private TbItemParamServiceApi tbItemParamServiceApi;

    //获取TbItemParam中数据总条数
    @Override
    public long selectCount() {
        return tbItemParamServiceApi.selectCount();
    }

    //获取TbItemParam中全部数据
    @Override
    public List<TbItemParam> selectAll(Integer page, Integer rows) {
        return tbItemParamServiceApi.selectAll(page, rows);
    }

    @Override
    public TbItemParam selectByItemCatId(long itemCatId) {
        return tbItemParamServiceApi.selectByItemCatId(itemCatId);
    }

    //添加指定类目商品规格参数
    @Override
    public int insertByItemCatId(TbItemParam tbItemParam) {
        return tbItemParamServiceApi.insertByItemCatId(tbItemParam);
    }

    //删除商品规格参数
    @Override
    public int deleteById(long[] ids) {
        return tbItemParamServiceApi.deleteById(ids);
    }
}
