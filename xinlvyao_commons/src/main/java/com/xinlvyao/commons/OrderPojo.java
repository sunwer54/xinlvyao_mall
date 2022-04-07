package com.xinlvyao.commons;

import com.xinlvyao.pojo.TbOrderItem;
import com.xinlvyao.pojo.TbOrderShipping;

import java.io.Serializable;
import java.util.List;

/**
 * @author: 享学课堂-SaiLing老师
 * @老师qq: 2408524688
 * @describe: 封装提交的结算信息数据的类
 */
public class OrderPojo implements Serializable {
    /*这个对象需要发送到rabbitmq队列中,需要序列码
    serialVersionUID适用于java序列化机制。
    简单来说，JAVA序列化的机制是通过 判断类的serialVersionUID来验证的版本一致的。
    在进行反序列化时，JVM会把传来的字节流中的serialVersionUID于本地相应实体类的serialVersionUID进行比较。
    如果相同说明是一致的，可以进行反序列化，否则会出现反序列化版本一致的异常，即是InvalidCastException。
    具体序列化的过程是这样的：序列化操作时会把系统当前类的serialVersionUID写入到序列化文件中，
    当反序列化时系统会自动检测文件中的serialVersionUID，判断它是否与当前类中的serialVersionUID一致。
    如果一致说明序列化文件的版本与当前类的版本是一样的，可以反序列化成功，否则就失败；
     */
    public static final long serialVersionUID = 1;
    private int paymentType;
    private String payment;
    private List<TbOrderItem> orderItems;
    private TbOrderShipping orderShipping;
    private long userId;
    private String orderID;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }

    @Override
    public String toString() {
        return "OrderPojo{" +
                "paymentType=" + paymentType +
                ", payment='" + payment + '\'' +
                ", orderItems=" + orderItems +
                ", orderShipping=" + orderShipping +
                ", userId=" + userId +
                ", orderID='" + orderID + '\'' +
                '}';
    }
}
