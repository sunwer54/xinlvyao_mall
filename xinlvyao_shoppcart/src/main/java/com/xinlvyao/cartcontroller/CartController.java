package com.xinlvyao.cartcontroller;

import com.xinlvyao.cartpojo.CartPojo;
import com.xinlvyao.cartpojo.OrderCartPojo;
import com.xinlvyao.cartservice.ItemService;
import com.xinlvyao.commons.JdResult;
import com.xinlvyao.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
public class CartController {

    @Autowired
    private ItemService itemService;

    /**
     * 将商品加入购物车
     * @param id :商品id
     * @param num ：商品数量
     * @param session
     * @return
     */
    @RequestMapping("/cart/add/{id}.html")
    public ModelAndView addCart(@PathVariable long id, int num, HttpSession session){
        ModelAndView m = null;
        //先判断用户是否是登录状态，有登录信息则直接加入购物车，否则跳转到登录页面
        TbUser user = (TbUser)session.getAttribute("logUser");
        if (user!=null){
            //有登录身份信息，可直接将商品加入购物车
            itemService.selectItemById(user,id,num);
            m = new ModelAndView(new RedirectView("/cartSuccess"));
        }else {
            //无登录信息，跳转到登录页面
            m = new ModelAndView(new RedirectView("http://localhost:8084/user/showLogin"));
        }
        return m;
    }

    /**
     * http://localhost:8085/cart/cart.html
     * 进入购物车
     */
    @RequestMapping("/cart/cart.html")
    public String gotoCart(Model model){
        Map<Long, CartPojo> cartItems = itemService.getCartItem();
        if (cartItems!=null) {
            //cartItems不为null，说明用户是登录状态，将redis中数据数据携带到cart.jsp页面
            Collection<CartPojo> values = cartItems.values();
            //将Collection集合转化为List集合
            List<CartPojo> cartLists = new ArrayList<>(values);
            model.addAttribute("cartList", cartLists);
        }
        return "cart";
    }

    /**
     * /cart/update/num/1646669639905/2.action
     * 在购物车中点击+或者-修改商品数量
     * @param id :商品id
     * @param num ：商品数量
     */
    @RequestMapping("/cart/update/num/{id}/{num}.action")
    @ResponseBody
    public JdResult incOrDecCartNum(@PathVariable long id,@PathVariable int num){
        itemService.updateCartNum(id, num);
        return JdResult.ok();
    }

    /**
     * /service/cart/update/num/1646669639905/10
     * 购物车直接输入数量修改
     * @param id :商品id
     * @param num ：商品数量
     * @param session
     */
    @RequestMapping("/service/cart/update/num/{id}/{num}")
    @ResponseBody
    public JdResult updateInputNum(@PathVariable long id,@PathVariable int num,HttpSession session){
        itemService.updateCartNum(id, num);
        return JdResult.ok();
    }

    /**
     * /cart/delete/1646669639905.action
     * 删除购物车中的商品
     * @param id :商品id
     */
    @RequestMapping("/cart/delete/{id}.action")
    @ResponseBody
    public JdResult deleteItem(@PathVariable long id){
        itemService.deleteCartItem(id);
        return JdResult.ok();
    }

    /**
     * http://localhost:8085/cart/order-cart.html?id=1646669639905&id=1646559832578
     * 从购物车跳转到结算页面
     * @RequestParam(value = "id")表示接收请求中name是id的value
     */
    @RequestMapping("/cart/order-cart.html")
    public String orderCart(@RequestParam(value = "id") long[] ids,Model model){
        //跳转到结算页面也需要携带购物车中的数据跳转,还需要验证库存是否足够
        List<OrderCartPojo> orderCarts = itemService.getOrderCart(ids);
        model.addAttribute("cartList",orderCarts);
        return "order-cart";
    }
}
