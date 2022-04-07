package com.xinlvyao.itemservice.impl;

import com.xinlvyao.api.ItemCatServiceApi;
import com.xinlvyao.itempojo.ItemCatAllNodeData;
import com.xinlvyao.itempojo.ItemCatNode;
import com.xinlvyao.itemservice.ItemCatService;
import com.xinlvyao.pojo.TbItemCat;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
/**
 * 提供前台门户导航的本地服务接口实现
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Reference
    private ItemCatServiceApi itemCatServiceApi;

    @Override
    @Cacheable(cacheNames = "com.jd.item",key = "'NodeData'")//将返回结果缓存到redis中
    public ItemCatAllNodeData getItemCatAllNodeData() {
        List<Object> list = this.selectAllItemCate((byte) 0);
        ItemCatAllNodeData itemCatAllNodeData = new ItemCatAllNodeData();
        itemCatAllNodeData.setData(list);
        return itemCatAllNodeData;
    }

    @Override
    public List<Object> selectAllItemCate(long parentId) {
        //查询到当前parentId值的全部类目信息
        List<TbItemCat> itemCats = itemCatServiceApi.selectAllItemCate(parentId);
        List<Object> list = new ArrayList<>();
        //遍历每个类目，并判断每个类目下是否有子类目
        for (TbItemCat itemCat:itemCats){
            //当前类目有子类目
            if (itemCat.getIsParent()==1){
                //创建类目节点对象，封装当前类目的信息
                ItemCatNode itemCatNode = new ItemCatNode();
                itemCatNode.setU("/products/"+itemCat.getId()+".html");
                itemCatNode.setN("<a href='/products/"+itemCat.getId()+".html'>"+itemCat.getName()+"</a>");
                //获取其子类目，并将其子类目存入i属性的集合中
                List<Object> childItemCats = this.selectAllItemCate(itemCat.getId());
                itemCatNode.setI(childItemCats);
                //将当前类目的节点对象作为元素，放入list集合中
                list.add(itemCatNode);
            }else {
                //当前类目没有子类目，直接将当前类目放入集合中
                list.add("/products/"+itemCat.getId()+".html|"+itemCat.getName());
            }
        }
        return list;
    }
}
