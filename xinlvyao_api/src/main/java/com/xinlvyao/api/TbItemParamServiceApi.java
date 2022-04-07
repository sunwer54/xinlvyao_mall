package com.xinlvyao.api;

import com.xinlvyao.pojo.TbItemParam;
import java.util.List;

/**
 * 提供后台商品规格参数中的各个操作（全部商品规格参数，添加商品规格参数，删除商品规格参数）的远程接口
 */
public interface TbItemParamServiceApi {

    //获取TbItemParam中商品规格参数数据总条数
    public long selectCount();

    //获取TbItemParam中全部商品参数数据
    public List<TbItemParam> selectAll(Integer page,Integer rows);

    //根据itemCatId（商品类目id）查询tbItemParam中商品规格规格参数信息
    public TbItemParam selectByItemCatId(long itemCatId);

    //添加指定类目商品规格参数
    public int insertByItemCatId(TbItemParam tbItemParam);

    //删除商品规格参数
    public int deleteById(long[] ids);
}
