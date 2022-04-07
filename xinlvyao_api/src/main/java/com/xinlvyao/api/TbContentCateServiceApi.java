package com.xinlvyao.api;

import com.xinlvyao.pojo.TbContentCategory;
import java.util.List;

/**
 * 提供后台内容分类管理中的各个操作（全部内容分类，新增内容分类，修改内容分类，删除内容分类）的远程接口
 */
public interface TbContentCateServiceApi {
    //获取全部内容分类
    public List<TbContentCategory> selectAllContentCate(long parentId);

    //新增内容分类
    public int insertContentCate(TbContentCategory tbContentCategory);

    //根据id修改分类的isParent为1
    public int updateContentCateIsParent(long id,byte isParent);

    //根据id删除内容分类
    public int deleteContentCate(long id);

    //根据id修改内容分类的名称
    public int updateContentCateName(long id,String name);
}
