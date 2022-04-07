package com.xinlvyao.provider;

import com.xinlvyao.api.TbOrderServiceApi;
import com.xinlvyao.mapper.TbOrderItemMapper;
import com.xinlvyao.mapper.TbOrderMapper;
import com.xinlvyao.mapper.TbOrderShippingMapper;
import com.xinlvyao.pojo.TbOrder;
import com.xinlvyao.pojo.TbOrderItem;
import com.xinlvyao.pojo.TbOrderShipping;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TbOrderServiceApiImpl implements TbOrderServiceApi {
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;
    @Autowired
    private TbOrderMapper orderMapper;
    @Override
    @Transactional
    public int insertOrderData(List<TbOrderItem> orderItems, TbOrderShipping orderShipping, TbOrder order) {
        for (TbOrderItem orderItem:orderItems){
            orderItemMapper.insert(orderItem);
        }
        orderShippingMapper.insert(orderShipping);
        orderMapper.insert(order);
        return 1;
    }
}
