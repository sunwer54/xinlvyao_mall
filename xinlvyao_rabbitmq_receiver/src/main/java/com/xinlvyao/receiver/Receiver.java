package com.xinlvyao.receiver;

import com.xinlvyao.commons.*;
import com.xinlvyao.service.OrderService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class Receiver {
    @Autowired
    private OrderService orderService;
    /**
     * 接收更新redis中广告的消息
     * @param adOfRedisMsg
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "updateAdOfRedis"),
                    exchange = @Exchange(value = "AdOfRedisExchange", type = ExchangeTypes.DIRECT)
            )
    )
    public void receiveAdOfRedisMsg(String adOfRedisMsg){
        System.out.println("接收到更新redis广告的消息："+adOfRedisMsg);
        String s = adOfRedisMsg.substring(adOfRedisMsg.indexOf("-")+1);
        if (adOfRedisMsg.startsWith("新增大广告成功")){
            System.out.println("s:"+s);
            HttpClientUtil.doPostJson("http://localhost:8082/update/ad/redis",s);
        }
    }

    /**
     * 接收更新索引库中商品信息的队列
     * @param esItemMsg
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "updateEsItem"),
                    exchange = @Exchange(value = "EsItemExchange", type = ExchangeTypes.DIRECT)
            )
    )
    public void receiveEsItemMsg(String esItemMsg){
        System.out.println("接收到更新索引库的消息："+esItemMsg);
        String s = esItemMsg.substring(esItemMsg.indexOf("-")+1);
        if (esItemMsg.startsWith("新增商品成功,更新es")) {
            System.out.println("s:"+s);
            HttpClientUtil.doPostJson("http://localhost:8083/update/item/es",s );
        }
    }

    /**
     * 接收创建订单的消息
     * @param orderMsg
     * @return
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "createOrderMsg"),
                    exchange = @Exchange(value = "createOrderExchange", type = ExchangeTypes.DIRECT)
            )
    )
    public boolean receiveOrderMsg(String orderMsg){
        System.out.println("接收到创建订单消息："+orderMsg);
        //处理接收到的创建的消息，在数据库中创建订单
        return orderService.createOrder(orderMsg);
    }

    /**
     * 接收删除购车和司改数据库库存的消息
     * @param updateOrderMsg
     * @return
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "updateOrderMsg"),
                    exchange = @Exchange(value = "createOrderExchange", type = ExchangeTypes.DIRECT)
            )
    )
    public boolean receiveUpdateOrder(String updateOrderMsg){
        System.out.println("接收到删除购物车和修改库存的消息"+updateOrderMsg);
        //把消息转化为对象
        List<UpdateOrderPojo> updateOrderPojos = JsonUtils.jsonToList(updateOrderMsg, UpdateOrderPojo.class);
        return orderService.delCartAndUpdateNum(updateOrderPojos);
    }

    /**
     * 接收发送短信的消息
     * @param sendSmsMsg
     * @return
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "sendSmsMsg"),
                    exchange = @Exchange(value = "createOrderExchange", type = ExchangeTypes.DIRECT)
            )
    )
    public boolean receiveSendSms(String sendSmsMsg){
        System.out.println("接收到发送短信的消息"+sendSmsMsg);
        //把消息转化为对象
        List<UpdateOrderPojo> updateOrderPojos = JsonUtils.jsonToList(sendSmsMsg, UpdateOrderPojo.class);
        if (updateOrderPojos!=null&&updateOrderPojos.size()!=0) {
            String phone = updateOrderPojos.get(0).getUser().getPhone();
            String msg = "您的订单已创建成功，已经在向你奔来,敬请等待收货!";
            try {
                SendSms.sendMessage(phone.trim(),msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    /**
     * 接收发送邮件的消息
     * @param sendEmailMsg
     * @return
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "sendEmailMsg"),
                    exchange = @Exchange(value = "createOrderExchange", type = ExchangeTypes.DIRECT)
            )
    )
    public boolean receiveSendEmail(String sendEmailMsg){
        System.out.println("接收到发送邮件的消息"+sendEmailMsg);
        //把消息转化为对象
        List<UpdateOrderPojo> updateOrderPojos = JsonUtils.jsonToList(sendEmailMsg, UpdateOrderPojo.class);
        if (updateOrderPojos!=null&&updateOrderPojos.size()!=0) {
            String email = updateOrderPojos.get(0).getUser().getEmail();
            String text = "您的订单已创建成功，已经在向你奔来,敬请等待收货!";
            String title = "订单成功通知";
            return SendMail.sendMailTo(email, text, title);
        }
        return false;
    }
}
