package com.xinlvyao.itemservice.impl;

import com.xinlvyao.api.TbItemServiceApi;
import com.xinlvyao.itemservice.TbItemService;
import com.xinlvyao.pojo.TbItem;
import com.xinlvyao.pojo.TbItemDesc;
import com.xinlvyao.pojo.TbItemParamItem;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

/**
 * 提供后台全部商品中的各个操作（全部商品，新增商品，上架商品，下架商品，删除商品）的本地TbItemService接口实现
 */
@Service
public class TbItemServiceImpl implements TbItemService {

    //从zookeeper中订阅服务
    @Reference
    private TbItemServiceApi tbItemServiceApi;

    /**
     * 根据id查询在TbItem表中查询商品信息
     * @param id
     * @return
     */
    @Override
    public TbItem selectItemById(long id) {
        return tbItemServiceApi.selectItemById(id);
    }

    /**
     * 根据itemId查询在TbItemDesc表中查询商品描述信息
     * @param itemId
     * @return
     */
    @Override
    public TbItemDesc selectItemDescByItemId(long itemId) {
        return tbItemServiceApi.selectItemDescByItemId(itemId);
    }

    /**
     * 根据itemId查询在TbItemParamItem表中查询商品参数信息
     * @param itemId
     * @return
     */
    @Override
    public TbItemParamItem selectItemParamByItemId(long itemId) {
        return tbItemServiceApi.selectItemParamByItemId(itemId);
    }
}
