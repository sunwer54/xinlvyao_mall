package com.xinlvyao.itemcontroller;

import com.xinlvyao.itempojo.ItemCatAllNodeData;
import com.xinlvyao.itemservice.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 提供前台门户导航的Controller
 */
@RestController//该注解中包含了@ResponseBody,表示该类中所有的方法都将响应json数据到页面
public class ItemCatController {

    @Autowired
    private ItemCatService itemCatService;

    /**
     * 前台门户商品类目导航
     * @return
     */
    @RequestMapping("/rest/itemcat/all")
    @CrossOrigin//该注解表示接收其他服务器向本服务器发送过来的请求（即接收跨域访问请求）
    public ItemCatAllNodeData showItem(){
        return itemCatService.getItemCatAllNodeData();
    }
}
