package com.xinlvyao.itemcontroller;

import com.xinlvyao.commons.JsonUtils;
import com.xinlvyao.itempojo.ParamItem;
import com.xinlvyao.itemservice.TbItemService;
import com.xinlvyao.pojo.TbItem;
import com.xinlvyao.pojo.TbItemDesc;
import com.xinlvyao.pojo.TbItemParamItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ItemController {

    @Autowired
    private TbItemService tbItemService;

    /**
     * 显示搜索商品的详细信息
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/item/{id}.html")
    @CrossOrigin
    public String showItem(@PathVariable long id, Model model){
        TbItem tbItem = tbItemService.selectItemById(id);
        List<String> images = new ArrayList<>();
        images.add(tbItem.getImage());
        tbItem.setImages(images);
        model.addAttribute("item",tbItem);
        return "item";
    }

    /**
     * 搜索商品的描述信息
     * @param itemId
     * @return
     */
    @RequestMapping("/item/desc/{itemId}.html")
    @ResponseBody
    public String showItemDesc(@PathVariable long itemId){
        TbItemDesc tbItemDesc = tbItemService.selectItemDescByItemId(itemId);
        return tbItemDesc.getItemDesc();
    }

    /**
     * 搜索商品的规格参数
     * @param itemId
     * @return
     */
    @RequestMapping("/item/param/{itemId}.html")
    @ResponseBody
    public String showItemParam(@PathVariable long itemId){
        TbItemParamItem itemParamItem = tbItemService.selectItemParamByItemId(itemId);
        //通过工具类把json格式的规格参数转成ParamItem对象
        if (itemParamItem!=null) {
            List<ParamItem> paramItems = JsonUtils.jsonToList(itemParamItem.getParamData(), ParamItem.class);
            //拼接把规格与数据拼接成表格table
            //往json数据中插入规格参数的格式
            StringBuffer sf = new StringBuffer();
            for (ParamItem paramItem : paramItems) {
                // 把一个ParamItem当作一个表格看待
                sf.append("<table style='color:gray;' width='100%' cellpadding='5'>");
                for (int i = 0; i < paramItem.getParams().size(); i++) {
                    sf.append("<tr>");
                    if (i == 0) {// 说明是第一行，第一行要显示分组信息
                        sf.append("<td style='width:100px;text-align:right;'>" + paramItem.getGroup() + "</td>");
                    } else {
                        // html列是空最好给个空格
                        sf.append("<td> </td>");// 除了第一行以外其他行第一列都是空。
                    }
                    sf.append("<td style='width:200px;text-align:right;'>" + paramItem.getParams().get(i).getK() + "</td>");
                    sf.append("<td>" + paramItem.getParams().get(i).getV() + "</td>");
                    sf.append("</tr>");
                }
                sf.append("</table>");
                sf.append("<hr style='color:gray;'/>");
            }
            return sf.toString();
        }
        return "";
    }
}
