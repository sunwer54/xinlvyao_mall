package com.xinlvyao.cartservice;


import com.xinlvyao.cartpojo.CartPojo;
import com.xinlvyao.cartpojo.OrderCartPojo;
import com.xinlvyao.pojo.TbUser;
import java.util.List;
import java.util.Map;

public interface ItemService {
    //根据id在TbItem表中查询商品信息
    public void selectItemById(TbUser user,long id, int num);

    //从redis中获取购物车商品信息
    public Map<Long,CartPojo> getCartItem();

    //在购物车中修改商品数量
    public void updateCartNum(long id,int num);

    //删除购物车中的商品
    public void deleteCartItem(long id);

    //获取跳转订单页面需要的数据
    public List<OrderCartPojo> getOrderCart(long[] ids);
}
