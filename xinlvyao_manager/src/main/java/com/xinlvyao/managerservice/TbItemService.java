package com.xinlvyao.managerservice;

import com.xinlvyao.pojo.TbItem;
import com.xinlvyao.pojo.TbItemCat;
import com.xinlvyao.pojo.TbItemDesc;
import com.xinlvyao.pojo.TbItemParamItem;

import java.util.Date;
import java.util.List;

/**
 * 提供后台全部商品中的各个操作（全部商品，新增商品，上架商品，下架商品，删除商品）的本地接口
 */
public interface TbItemService {
    //查询商品数据总条数
    public long selectTbItemCount();

    //查询全部商品数据并使用分页
    public List<TbItem> selectAllTbItem(int page, int rows);

    //下架商品
    public int downUpdateTbItem(long[] ids, int status, Date updated);

    //上架商品
    public int upUpdateTbItem(long[] ids, int status, Date updated);

    //删除商品
    public int deleteUpdateTbItem(long[] ids, int status, Date updated);

    //新增商品时的类目选择
    public List<TbItemCat> selectCat(long parentId);

    //提交新增商品（提交商品数据到TbItem表，提交商品描述到TbItemDesc表，提交商品参数到TbItemParamItem表）
    public int insertProduct(TbItem tbItem, TbItemDesc tbItemDesc, TbItemParamItem tbItemParamItem);
}
