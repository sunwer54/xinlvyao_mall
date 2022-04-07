package com.xinlvyao.passportcontroller;

import com.xinlvyao.commons.JdResult;
import com.xinlvyao.passportservice.UserService;
import com.xinlvyao.pojo.TbUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 处理登录请求
     * @param tbUser
     * @param session
     * @return
     */
    @RequestMapping("/user/login")
    @ResponseBody//响应json格式字符串到页面
    public JdResult userLogin(TbUser tbUser, HttpSession session){
        //查询数据库是否含有该用户信息
        TbUser u = userService.loginUser(tbUser);
        if (u!=null){
            //登录成功,把登录身份放入session中,SpringSession会把这个数据放入redis中
            session.setAttribute("logUser",u);
            return JdResult.ok();
        }
        return JdResult.error("用户名或密码错误");
    }

    /**
     * 登录成功后各个微服务请求登录者身份信息
     * @param session
     * @return
     */
    @RequestMapping("/user/token")
    @CrossOrigin(allowCredentials="true") //allowCredentials="true"允许客户端的请求携带cookie
    @ResponseBody
    public JdResult getUserInfoToPortal(HttpSession session){
        TbUser user = (TbUser)session.getAttribute("logUser");
        if (user!=null){
            return JdResult.ok(user);
        }
        return JdResult.error("获取用户信息失败");
    }

    /**
     * 退出登录
     * @param session
     * @return
     */
    @RequestMapping("/user/logout")
    @CrossOrigin//允许接收跨域请求
    public String outLogin(HttpSession session){
        //强制销毁session,redis中存储session数据的key会进入倒计时销毁
        session.invalidate();
        return "login";
    }
}
