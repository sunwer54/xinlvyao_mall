package com.xinlvyao.cartservice.impl;

import com.xinlvyao.api.TbItemServiceApi;
import com.xinlvyao.cartpojo.CartPojo;
import com.xinlvyao.cartpojo.OrderCartPojo;
import com.xinlvyao.cartservice.ItemService;
import com.xinlvyao.pojo.TbItem;
import com.xinlvyao.pojo.TbUser;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl implements ItemService {
    @Reference
    private TbItemServiceApi itemServiceApi;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public void selectItemById(TbUser user, long id, int num) {
        //首先判断购物车（redis）中是否已经有该商品，根据key取出value
        String key = user.getId() + "rediscart";
        Map<Long,CartPojo> cartItems = redisTemplate.boundHashOps(key).entries();
        //如果该用户已经有购物车并且已经有该商品，则在该商品的数量上加上num,然后再把该商品放入redis中，覆盖原来的那个
        if (cartItems!=null&&cartItems.containsKey(id)){
            //通过该商品id获取到对应的商品
            CartPojo cartPojo = cartItems.get(id);
            //将该商品的数量加上num
            cartPojo.setNum(cartPojo.getNum()+num);
            //再将map集合放入redis中
            redisTemplate.boundHashOps(key).putAll(cartItems);
            System.out.println("购物车已存在该商品，已将商品数量叠加");
        }else {
            //如果不存在购物车，或者购物车中没有该商品信息，将该商品加入购物车
            TbItem tbItem = itemServiceApi.selectItemById(id);
            CartPojo cartPojo = new CartPojo();
            cartPojo.setTitle(tbItem.getTitle());
            cartPojo.setPrice(tbItem.getPrice());
            cartPojo.setImage(tbItem.getImage());
            cartPojo.setNum(num);
            cartPojo.setId(id);
            //把该商品放入redis中
            redisTemplate.boundHashOps(key).put(id,cartPojo);
            System.out.println("商品成功加入购物车");
        }
    }

    //获取用户下的购物车中商品
    @Override
    public Map<Long,CartPojo> getCartItem() {
        /*
        先验证用户是否登录，是则从redis中获取购物车商品，并将购物车商品集合返回，否则直接返回一个null
         */
        //在Service层获取HttpSession对象的方式
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        HttpSession session = request.getSession();
        //获取用户登录信息
        TbUser user = (TbUser)session.getAttribute("logUser");
        if (user!=null){
            //如果用户处于登录状态，则取出redis中的数据
            String key = user.getId() + "rediscart";
            Map<Long,CartPojo> cartItems = redisTemplate.boundHashOps(key).entries();
            return cartItems;
        }
        //如果用户没有在登录状态，则无需获取redis中数据，直接返回null
        return null;
    }

    //在购物车中修改商品数量
    @Override
    public void updateCartNum(long id, int num) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        HttpSession session = request.getSession();
        TbUser user = (TbUser)session.getAttribute("logUser");
        String key = user.getId() + "rediscart";
        Map<Long,CartPojo> cartItems=redisTemplate.boundHashOps(key).entries();
        CartPojo cartPojo = cartItems.get(id);
        cartPojo.setNum(num);
        redisTemplate.boundHashOps(key).put(id,cartPojo);
    }

    //删除购物车中的商品
    @Override
    public void deleteCartItem(long id) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        HttpSession session = request.getSession();
        TbUser user = (TbUser)session.getAttribute("logUser");
        String key = user.getId() + "rediscart";
        redisTemplate.boundHashOps(key).delete(id);
    }

    //获取跳转订单页面需要的数据
    @Override
    public List<OrderCartPojo> getOrderCart(long[] ids) {
        List<OrderCartPojo> orderCarts = new ArrayList<>();
        Map<Long, CartPojo> cartItems = getCartItem();
        if (cartItems!=null&&cartItems.size()!=0) {
            for (long id : ids) {
                CartPojo cartPojo = cartItems.get(id);
                TbItem tbItem = itemServiceApi.selectItemById(id);
                boolean flag = tbItem.getNum() - cartPojo.getNum() > 0;
                OrderCartPojo orderCartPojo = new OrderCartPojo();
                orderCartPojo.setImage(cartPojo.getImage());
                orderCartPojo.setPrice(cartPojo.getPrice());
                orderCartPojo.setTitle(cartPojo.getTitle());
                orderCartPojo.setNum(cartPojo.getNum());
                orderCartPojo.setEnough(flag);
                orderCartPojo.setId(id);
                orderCarts.add(orderCartPojo);
                orderCartPojo.setId(id);
            }
            return orderCarts;
        }
        return null;
    }
}
