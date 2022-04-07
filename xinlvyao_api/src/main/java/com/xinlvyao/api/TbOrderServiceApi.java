package com.xinlvyao.api;

import com.xinlvyao.pojo.TbOrder;
import com.xinlvyao.pojo.TbOrderItem;
import com.xinlvyao.pojo.TbOrderShipping;

import java.util.List;

public interface TbOrderServiceApi {
    public int insertOrderData(List<TbOrderItem> orderItems, TbOrderShipping orderShipping, TbOrder order);
}
