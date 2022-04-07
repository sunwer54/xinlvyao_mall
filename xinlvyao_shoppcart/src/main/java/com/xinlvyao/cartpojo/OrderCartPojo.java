package com.xinlvyao.cartpojo;

/**
 * 封装结算数据
 */
public class OrderCartPojo extends CartPojo {
    //显示是否有货信息:true有货,false无货
    private boolean enough;

    public boolean isEnough() {
        return enough;
    }

    public void setEnough(boolean enough) {
        this.enough = enough;
    }
}
