package com.xinlvyao.api;

import com.xinlvyao.pojo.TbContent;

import java.util.List;

/**
 * 提供后台内容管理中的各个操作（全部内容，新增内容，编辑内容，删除内容）的远程接口
 */
public interface TbContentServiceApi {

    //获取全部内容
    public List<TbContent> selectAllContent(long categoryId,int page,int rows);

    //获取内容的数据总数
    public long selectCount(long categoryId);

    //新增内容
    public int insertContent(TbContent tbContent);

    //删除内容
    public int deleteContent(long[] ids);

    //编辑内容
    public int updateContent(TbContent tbContent);
}
