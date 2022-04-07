package com.xinlvyao.service;

import com.xinlvyao.commons.UpdateOrderPojo;

import java.util.List;

public interface OrderService {
    //创建订单
    public boolean createOrder(String orderMsg);

    //删除redis购物车对应的商品数据,修改数据库中对应商品的库存
    public boolean delCartAndUpdateNum(List<UpdateOrderPojo> updateOrderPojos);
}
