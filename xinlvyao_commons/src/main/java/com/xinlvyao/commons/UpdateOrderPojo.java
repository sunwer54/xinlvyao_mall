package com.xinlvyao.commons;

import com.xinlvyao.pojo.TbUser;

import java.io.Serializable;

public class UpdateOrderPojo implements Serializable {
    private TbUser user;
    private long itemID;
    private int num;

    public TbUser getUser() {
        return user;
    }

    public void setUser(TbUser user) {
        this.user = user;
    }

    public long getItemID() {
        return itemID;
    }

    public void setItemID(long itemID) {
        this.itemID = itemID;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "UpdateOrderPojo{" +
                "user=" + user +
                ", itemID=" + itemID +
                ", num=" + num +
                '}';
    }
}
