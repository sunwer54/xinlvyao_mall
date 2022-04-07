package com.xinlvyao.managercontroller;

import com.xinlvyao.commons.EasyUITree;
import com.xinlvyao.commons.JdResult;
import com.xinlvyao.managerservice.TbContentCateService;
import com.xinlvyao.pojo.TbContentCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 提供后台内容分类管理中的各个操作（全部内容分类，新增内容分类，修改内容分类，删除内容分类）的Conreoller
 */
@RestController//该注解中包含了@ResponseBody,表示该类中所有的方法都将响应json数据到页面
public class TbContentCateController {

    //注入本地接口服务对象
    @Autowired
    private TbContentCateService contentCateService;

    /**
     * 获取内容分类
     * @RequestParam(defaultValue = "0") long id：请求中若无提交id则默认id为0，若提交id则接收请求中的id
     * @return
     */
    @RequestMapping("/content/category/list")
    public List<EasyUITree> getContentCatList(@RequestParam(defaultValue = "0") long id){
        //执行内容分类查询
        List<TbContentCategory> tbContentCats = contentCateService.selectAllContentCate(id);
        //封装要响应到前端的数据
        List<EasyUITree> easyUITreeList = new ArrayList<>();
        //遍历，根据isParent判断当前分类下是否有子分类
        for(TbContentCategory tbContentCat:tbContentCats){
            EasyUITree easyUITree = new EasyUITree();
            easyUITree.setId(tbContentCat.getId());
            Byte isParent = tbContentCat.getIsParent();
            easyUITree.setState(isParent==1?"closed":"open");
            easyUITree.setText(tbContentCat.getName());
            easyUITreeList.add(easyUITree);
        }
        return easyUITreeList;
    }

    /**
     * 新增内容分类
     * @param parentId ：新增内容分类所属父分类的id
     * @param name ：新增内容分类的名称
     * @return
     */
    @PostMapping("/content/category/create")
    public JdResult createCate(long parentId, String name){
        //封装新增的内容分类的数据
        TbContentCategory tbContentCategory = new TbContentCategory();
        tbContentCategory.setParentId(parentId);
        tbContentCategory.setName(name);
        tbContentCategory.setStatus(1);
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setIsParent((byte)0);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());
        //执行内容分类新增
        int n = contentCateService.insertContentCate(tbContentCategory);
        //将父分类的isParent值改为1
        contentCateService.updateContentCateIsParent(parentId,(byte)1);
        //封装响应数据
        JdResult jdResult = new JdResult();
        //新增内容分类成功
        if (n==1){
            jdResult.setStatus(200);
            jdResult.setMsg("新增内容分类成功");
            jdResult.setData(tbContentCategory);
        }else {
            //新增内容分类失败
            jdResult.setStatus(400);
            jdResult.setMsg("新增内容分类失败");
            jdResult.setData(400);
        }
        return jdResult;
    }

    /**
     * 根据id删除内容分类
     * @param id ：删除的分类的id
     * @return
     */
    @PostMapping("/content/category/delete")
    public JdResult deleteCate(long id){
        //执行内容分类删除
        int n = contentCateService.deleteContentCate(id);
        //封装响应数据
        JdResult jdResult = new JdResult();
        //删除内容分类成功
        if (n==1){
            jdResult.setStatus(200);
            jdResult.setMsg("删除内容分类成功");
            jdResult.setData(200);
        }else {
            //删除内容分类失败
            jdResult.setStatus(400);
            jdResult.setMsg("删除内容分类失败");
            jdResult.setData(400);
        }
        return jdResult;
    }

    /**
     * 重命名分类的名称
     * @param name：重命名的分类的新名称
     * @param id:需要被重命名的分类的id
     * @return
     */
    @PostMapping("/content/category/update")
    public JdResult renameCate(long id,String name){
        //执行分类名称修改
        int n = contentCateService.updateContentCateName(id, name);
        //封装响应数据
        JdResult jdResult = new JdResult();
        //分类名称修改成功
        if (n==1){
            jdResult.setStatus(200);
            jdResult.setMsg("分类名称修改成功");
            jdResult.setData(200);
        }else {
            //分类名称修改失败
            jdResult.setStatus(400);
            jdResult.setMsg("分类名称修改失败");
            jdResult.setData(400);
        }
        return jdResult;
    }

}
