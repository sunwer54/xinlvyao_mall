package com.xinlvyao.managerservice.impl;

import com.xinlvyao.api.TbItemServiceApi;
import com.xinlvyao.pojo.TbItem;
import com.xinlvyao.pojo.TbItemCat;
import com.xinlvyao.pojo.TbItemDesc;
import com.xinlvyao.pojo.TbItemParamItem;
import com.xinlvyao.managerservice.TbItemService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
/**
 * 提供后台全部商品中的各个操作（全部商品，新增商品，上架商品，下架商品，删除商品）的本地TbItemService接口实现
 */
@Service
public class TbItemServiceImpl implements TbItemService {

    //从zookeeper中订阅服务
    @Reference
    private TbItemServiceApi tbItemServiceApi;

    /**
     * 查询商品数据总条数
     */
    @Override
    public long selectTbItemCount() {
        return tbItemServiceApi.selectTbItemCount();
    }

    /**
     *查询Tb_Item表中所有的数据并使用分页
     * @param page 页码
     * @param rows 每页显示数据条数
     * @return
     */
    @Override
    public List<TbItem> selectAllTbItem(int page, int rows) {
        return tbItemServiceApi.selectAllTbItem(page, rows);
    }

    /**
     * 根据商品id下架商品,状态码(1-正常，2-下架，3-删除)
     * @param ids 是需要修改状态的id
     */
    @Override
    public int downUpdateTbItem(long[] ids, int status, Date updated) {
        return tbItemServiceApi.downUpdateTbItem(ids,status,updated);
    }

    /**
     * 根据商品id上架商品,状态码(1-正常，2-下架，3-删除)
     * @param ids 是需要修改状态的id
     */
    @Override
    public int upUpdateTbItem(long[] ids, int status, Date updated) {
        return tbItemServiceApi.upUpdateTbItem(ids,status,updated);
    }

    /**
     * 根据商品id删除商品,状态码(1-正常，2-下架，3-删除)
     * @param ids 是需要修改状态的id
     */
    @Override
    public int deleteUpdateTbItem(long[] ids, int status, Date updated) {
        return tbItemServiceApi.deleteUpdateTbItem(ids,status,updated);
    }

    /**
     * 新增商品时的类目选择
     * @param parentId
     * @return
     */
    @Override
    public List<TbItemCat> selectCat(long parentId) {
        return tbItemServiceApi.selectCat(parentId);
    }

    /**
     * 提交新增商品（提交商品数据到TbItem表，提交商品描述到TbItemDesc表，提交商品参数到TbItemParamItem表）
     * @param tbItem
     * @param tbItemDesc
     * @param tbItemParamItem
     * @return
     */
    @Override
    public int insertProduct(TbItem tbItem,TbItemDesc tbItemDesc, TbItemParamItem tbItemParamItem) {
        return tbItemServiceApi.insertProduct(tbItem,tbItemDesc,tbItemParamItem);
    }
}
