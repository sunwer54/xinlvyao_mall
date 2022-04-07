package com.xinlvyao.itemservice;

import com.xinlvyao.itempojo.ItemCatAllNodeData;

import java.util.List;
/**
 * 提供前台门户导航的本地服务接口
 */
public interface ItemCatService {

    public ItemCatAllNodeData getItemCatAllNodeData();

    public List<Object> selectAllItemCate(long parentId);
}
