package com.xinlvyao.managercontroller;

import com.google.gson.Gson;
import com.xinlvyao.commons.EasyUIDatagrid;
import com.xinlvyao.commons.JdResult;
import com.xinlvyao.pojo.TbContent;
import com.xinlvyao.managerservice.TbContentService;
import com.xinlvyao.sender.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
/**
 * 提供后台内容管理中的各个操作（全部内容，新增内容，编辑内容，删除内容）的Controller
 */
@RestController//该注解中包含了@ResponseBody,表示该类中所有的方法都将响应json数据到页面
public class TbContentController {

    //注入本地接口服务对象
    @Autowired
    private TbContentService contentService;

    @Autowired
    private Sender sender;
    /**
     * 获取TbContent表中category_id=categoryId的全部内容并分页显示
     * @param categoryId ：根据categoryId条件查询
     * @param page ：页码
     * @param rows ：每页数据条数
     * @return
     */
    @RequestMapping("/content/query/list")
    public EasyUIDatagrid queryContentList(long categoryId,int page,int rows){
        //根据categoryId查询全部内容并分页
        List<TbContent> allContent = contentService.selectAllContent(categoryId,page, rows);
        //查询TbContent表中category_id=categoryId的内容数据总数
        long contentTotal = contentService.selectCount(categoryId);
        //封装并返回响应数据
        return new EasyUIDatagrid(contentTotal,allContent);
    }

    /**
     * 新增内容
     * @param tbContent
     * @return
     */
    @PostMapping("/content/save")
    public JdResult saveContent(TbContent tbContent){
        //执行内容新增
        tbContent.setCreated(new Date());
        tbContent.setUpdated(new Date());
        int n = contentService.insertContent(tbContent);
        //封装响应数据
        JdResult jdResult = new JdResult();
        //新增内容成功
        if (n==1){
            jdResult.setStatus(200);
            jdResult.setMsg("新增内容成功");
            jdResult.setData(tbContent);
            //新增成功后，发送消息，同步更新redis中的大广告数据
            sender.updateAdOfRedisMsg("updateAdOfRedis","新增大广告成功-"+new Gson().toJson(tbContent));
        }else {
            //新增内容失败
            jdResult.setStatus(400);
            jdResult.setMsg("新增内容失败");
            jdResult.setData(400);
        }
        return jdResult;
    }

    /**
     * 根据id删除内容
     * @param ids
     * @return
     */
    @PostMapping("/content/delete")
    public JdResult deleteContent(long[] ids){
        //执行删除
        int n = contentService.deleteContent(ids);
        //封装响应数据
        JdResult jdResult = new JdResult();
        //删除内容成功
        if (n==1){
            jdResult.setStatus(200);
            jdResult.setMsg("删除内容成功");
            jdResult.setData(200);
        }else {
            //删除内容失败
            jdResult.setStatus(400);
            jdResult.setMsg("删除内容失败");
            jdResult.setData(400);
        }
        return jdResult;
    }

    /**
     * 编辑内容
     * @param tbContent：要编辑后的内容数据
     * @return
     */
    @PostMapping("/rest/content/edit")
    public JdResult editContent(TbContent tbContent){
        //执行内容编辑
        tbContent.setUpdated(new Date());
        int n = contentService.updateContent(tbContent);
        //封装响应数据
        JdResult jdResult = new JdResult();
        //内容编辑成功
        if (n==1){
            jdResult.setStatus(200);
            jdResult.setMsg("内容编辑成功");
            jdResult.setData(200);
        }else {
            //内容编辑失败
            jdResult.setStatus(400);
            jdResult.setMsg("内容编辑失败");
            jdResult.setData(400);
        }
        return jdResult;
    }
}
