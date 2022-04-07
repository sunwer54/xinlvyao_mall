package com.xinlvyao.itemservice;

import com.xinlvyao.pojo.TbItem;
import com.xinlvyao.pojo.TbItemDesc;
import com.xinlvyao.pojo.TbItemParamItem;


public interface TbItemService{
    //根据id查询在TbItem表中查询商品信息
    public TbItem selectItemById(long id);

    //根据itemId查询在TbItemDesc表中查询商品描述信息
    public TbItemDesc selectItemDescByItemId(long itemId);

    //根据itemId查询在TbItemParamItem表中查询商品参数信息
    public TbItemParamItem selectItemParamByItemId(long itemId);
}
