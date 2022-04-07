package com.xinlvyao.tradecontroller;

import com.xinlvyao.commons.IDUtils;
import com.xinlvyao.commons.JsonUtils;
import com.xinlvyao.commons.OrderPojo;
import com.xinlvyao.commons.UpdateOrderPojo;
import com.xinlvyao.pojo.TbOrderItem;
import com.xinlvyao.pojo.TbUser;
import com.xinlvyao.sender.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class OrderController {
    @Autowired
    private Sender sender;
    //创建订单消息的队列名称
    private String createOrder = "createOrderMsg";
    private String updateOrder = "updateOrderMsg";
    private String sendEmail = "sendEmailMsg";
    private String sendSMS = "sendSmsMsg";
    /**
     * http://localhost:8086/order/create.html
     * 创建订单：创建订单后需要把订单相关信息提交到数据库中
     * @param orderPojo :提交的订单相关参数
     */
    @PostMapping("/order/create.html")
    public String createOrder(OrderPojo orderPojo, HttpSession session, Model model){
        //发送创建订单的队列消息（订单相关数据），订单创建成后再反馈结果回来，然后需要扣库存，以及删除购物车数据等
        //1.获取登录身份信息
        TbUser user = (TbUser) session.getAttribute("logUser");
        //2.把用户id放入OrderPojo实体类中
        orderPojo.setUserId(user.getId());
        //3.创建订单id，准备创建订单时使用
        orderPojo.setOrderID(IDUtils.genItemId()+"");
        //发送创建订单的消息，把创建订单的数据通过消息发送者发送给消息消费者
        boolean orderFlag = (boolean)sender.createOrderMsg(createOrder, JsonUtils.objectToJson(orderPojo));
        System.out.println("订单创建消息发送成功");
        //订单创建成功后，发送消息修改数据库库存，清理购物车商品数据，发送邮件，发送短信等，携带数据跳转页面
        if (orderFlag){
            System.out.println("订单创建成功,12小时内将给您发货");
            //封装页面需要的数据
            Map<String,Object> map = new HashMap<>();
            map.put("payment",orderPojo.getPayment());
            map.put("date",new Date());
            map.put("orderId",orderPojo.getOrderID());
            //携带基本类型的数据到页面使用该方法添加数据
            model.addAllAttributes(map);
            //删除redis中购物车商品数据（userID,itemId）和修改数据库中商品的库存（itemId,num）
            List<UpdateOrderPojo> updateOrderPojos = new ArrayList<>();
            List<TbOrderItem> orderItems = orderPojo.getOrderItems();
            for (TbOrderItem orderItem:orderItems){
                //封装要发送的消息的数据
                UpdateOrderPojo updateOrderPojo = new UpdateOrderPojo();
                updateOrderPojo.setUser(user);
                updateOrderPojo.setItemID(Long.parseLong(orderItem.getItemId()));
                updateOrderPojo.setNum(orderItem.getNum());
                updateOrderPojos.add(updateOrderPojo);
            }
            String updateOderJson = JsonUtils.objectToJson(updateOrderPojos);
            //发送清理购物车和扣库存的消息
            boolean updateFlag = (boolean)sender.createOrderMsg(updateOrder, updateOderJson);
            System.out.println("删除购物车和修改数据库库存的消息发送成功");
            if (updateFlag){
                System.out.println("删除购物车和修改数据库库存完成");
            }
            //发送发送邮件的消息
            boolean emailFlag = (boolean)sender.createOrderMsg(sendEmail, updateOderJson);
            System.out.println("发送发送邮件的消息队列");
            if (emailFlag){
                System.out.println("邮件消息发送成功");
            }
            //发送发送短信的消息
            boolean smsFlag = (boolean)sender.createOrderMsg(sendSMS, updateOderJson);
            if (smsFlag){
                System.out.println("短信消息发送成功");
            }
        }
        return "success";
    }
}
