package com.xinlvyao.service.impl;

import com.xinlvyao.api.TbItemServiceApi;
import com.xinlvyao.api.TbOrderServiceApi;
import com.xinlvyao.commons.IDUtils;
import com.xinlvyao.commons.JsonUtils;
import com.xinlvyao.commons.OrderPojo;
import com.xinlvyao.commons.UpdateOrderPojo;
import com.xinlvyao.pojo.TbItem;
import com.xinlvyao.pojo.TbOrder;
import com.xinlvyao.pojo.TbOrderItem;
import com.xinlvyao.pojo.TbOrderShipping;
import com.xinlvyao.service.OrderService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Reference
    private TbOrderServiceApi orderServiceApi;
    @Reference
    private TbItemServiceApi itemServiceApi;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 创建订单
     * @param orderMsg :创建订单需要的消息数据
     * @return
     */
    @Override
    public boolean createOrder(String orderMsg) {
        /*处理接收到的创建的消息，在数据库中创建订单*/
        //1.把接收到的消息转化为对象
        OrderPojo orderPojo = JsonUtils.jsonToPojo(orderMsg, OrderPojo.class);
        //2.再次判断库存是否足够
        if (orderPojo==null){
            return false;
        }
        List<TbOrderItem> orderItems = orderPojo.getOrderItems();
        for (TbOrderItem orderItem:orderItems) {
            //判断库存是否足够
            TbItem tbItem = itemServiceApi.selectItemById(Long.parseLong(orderItem.getItemId()));
            //库存不够，创建订单失败，返回false
            if (tbItem.getNum()-orderItem.getNum()<0){
                return false;
            }
        }
        //3.封装要插入到TbOrder，TbOrderItem，TbOrderShipping三张表中的数据
        //3.1-TbOrder表
        //给TbOrder中的各属性赋值，各创建订单信息
        TbOrder order = new TbOrder();
        order.setOrderId(orderPojo.getOrderID());
        order.setPayment(orderPojo.getPayment());
        order.setPaymentType(orderPojo.getPaymentType());
        order.setPaymentTime(new Date());
        order.setCreateTime(new Date());
        order.setUserId(orderPojo.getUserId());
        order.setStatus(2);
        //3.2-TbOrderItem数据
        for (TbOrderItem orderItem:orderItems) {
            //给TbOrderItem（各商品订单明细）中的OrderId和Id设置数据
            orderItem.setOrderId(orderPojo.getOrderID());
            orderItem.setId(IDUtils.genItemId()+"");
        }
        //3.3-TbOrderShipping表
        TbOrderShipping orderShipping = orderPojo.getOrderShipping();
        orderShipping.setOrderId(orderPojo.getOrderID());
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        int n = orderServiceApi.insertOrderData(orderItems, orderShipping, order);
        return n == 1;
    }

    /**
     * 删除redis购物车对应的商品数据,修改数据库中对应商品的库存
     * @param updateOrderPojos
     * @return
     */
    @Override
    public boolean delCartAndUpdateNum(List<UpdateOrderPojo> updateOrderPojos){
        //把消息转化为对象
        String key = updateOrderPojos.get(0).getUser().getId() + "rediscart";
        int update = 0;
        long delete = 0;
        for (UpdateOrderPojo updateOrderPojo:updateOrderPojos){
            long itemID = updateOrderPojo.getItemID();
            TbItem tbItem = itemServiceApi.selectItemById(itemID);
            Integer sum = tbItem.getNum();
            int num = sum - updateOrderPojo.getNum();
            int m = itemServiceApi.updateItemNum(updateOrderPojo.getItemID(),num);
            update+=m;
            Long n = redisTemplate.boundHashOps(key).delete(itemID);
            delete+=n;
        }
        return update == updateOrderPojos.size() && delete == updateOrderPojos.size();
    }
}
