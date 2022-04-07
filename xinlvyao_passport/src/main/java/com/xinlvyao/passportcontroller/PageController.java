package com.xinlvyao.passportcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    /**
     * 处理前台页面点击登录跳转到登录页面
     * @return
     */
    @RequestMapping("/user/showLogin")
    @CrossOrigin
    public String showLogin(){
        return "login";
    }

    @RequestMapping("/{page}")
    @CrossOrigin
    public String showLogin(@PathVariable String page){
        return page;
    }
}
