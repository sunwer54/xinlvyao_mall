package com.xinlvyao.managercontroller;

import com.xinlvyao.commons.JdResult;
import com.xinlvyao.pojo.Manager;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ManagerUserController {
    @RequestMapping("/loginManager")
    @ResponseBody
    public JdResult manageUserLogin(HttpServletRequest request){
        //方式一(不严谨):
       /*  Object attribute = req.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        if(attribute == null){
            return "back-index";
        }else{
            return "redirect:login";
        }*/
        //方式二:从shiro的SessionManger中获取用户的信息
        Subject subject = SecurityUtils.getSubject();
        Manager manager = (Manager)subject.getPrincipal();
        if(manager != null){
            return JdResult.ok("登录成功");
        }else{
            return JdResult.error("用户名或密码错误");
        }
    }
}
