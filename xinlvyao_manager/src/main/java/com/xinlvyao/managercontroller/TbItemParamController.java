package com.xinlvyao.managercontroller;

import com.xinlvyao.commons.EasyUIDatagrid;
import com.xinlvyao.commons.JdResult;
import com.xinlvyao.pojo.TbItemParam;
import com.xinlvyao.managerservice.TbItemParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
/**
 * 提供后台商品规格参数中的各个操作（全部商品规格参数，添加商品规格参数，删除商品规格参数）的controller
 */
@RestController//该注解中包含了@ResponseBody,表示该类中所有的方法都将响应json数据到页面
public class TbItemParamController {

    //注入本地TbItemParamService接口对象
    @Autowired
    private TbItemParamService tbItemParamService;

    /**
     * 获取TbItemParam中全部数据并使用分页
     * @param page：页码
     * @param rows：每页数据条数
     * @return
     */
    @RequestMapping("/item/param/list")
    public EasyUIDatagrid getParamList(Integer page, Integer rows){
        //获取TbItemParam中数据总条数
        long total = tbItemParamService.selectCount();
        //获取TbItemParam中全部数据
        List<TbItemParam> tbItemParams = tbItemParamService.selectAll(page, rows);
        //EasyUIDatagrid对象封装数据响应到前端页面
        return new EasyUIDatagrid(total,tbItemParams);
    }

    /**
     * 根据itemCatId查询tbItemParam信息
     * @param id
     * @return
     */
    @RequestMapping("/item/param/query/itemcatid/{id}")
    public JdResult queryItemParam(@PathVariable long id){
        TbItemParam tbItemParam = tbItemParamService.selectByItemCatId(id);
        return JdResult.ok(tbItemParam);
    }

    /**
     * 为TbItemCat表中指定id的商品类目添加商品规格参数
     * @param id ：TbItemCat表中指定id
     * @param paramData：商品规格参数信息
     * @return
     */
    @RequestMapping("/item/param/save/{id}")
    public JdResult saveParam(@PathVariable long id,String paramData){
        //创建TbItemParam对象封装要上传的数据
        TbItemParam tbItemParam = new TbItemParam();
        tbItemParam.setItemCatId(id);
        tbItemParam.setParamData(paramData);
        tbItemParam.setCreated(new Date());
        tbItemParam.setUpdated(new Date());
        //执行数据上传
        int n = tbItemParamService.insertByItemCatId(tbItemParam);
        //封装响应数据
        JdResult jdResult = new JdResult();
        //新增商品规格参数成功
        if (n==1){
            jdResult.setStatus(200);
            jdResult.setMsg("新增商品规格参数成功");
            jdResult.setData(tbItemParam);
        }else {
            //新增内容失败
            jdResult.setStatus(400);
            jdResult.setMsg("新增商品规格参数失败");
            jdResult.setData(400);
        }
        return jdResult;
    }

    //删除商品规格参数
    @PostMapping("/item/param/delete")
    public JdResult deleteItemParam(long[] ids){
        //执行删除
        int n = tbItemParamService.deleteById(ids);
        //封装响应数据
        JdResult jdResult = new JdResult();
        //新增参数信息成功
        if (n==1){
            jdResult.setStatus(200);
            jdResult.setMsg("删除参数信息成功");
            jdResult.setData(200);
        }else {
            //新增内容失败
            jdResult.setStatus(400);
            jdResult.setMsg("删除参数信息失败");
            jdResult.setData(400);
        }
        return jdResult;
    }
}
