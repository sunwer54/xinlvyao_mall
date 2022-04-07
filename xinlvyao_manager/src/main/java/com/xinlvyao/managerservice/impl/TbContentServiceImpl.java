package com.xinlvyao.managerservice.impl;

import com.xinlvyao.api.TbContentServiceApi;
import com.xinlvyao.pojo.TbContent;
import com.xinlvyao.managerservice.TbContentService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * 提供后台内容管理中的各个操作（全部内容，新增内容，编辑内容，删除内容）的TbContentService本地接口实现
 */
@Service
public class TbContentServiceImpl implements TbContentService {

    //订阅远程服务
    @Reference
    private TbContentServiceApi contentServiceApi;

    //获取全部内容
    @Override
    public List<TbContent> selectAllContent(long categoryId, int page, int rows) {
        return contentServiceApi.selectAllContent(categoryId,page, rows);
    }

    //获取内容的数据总数
    @Override
    public long selectCount(long categoryId) {
        return contentServiceApi.selectCount(categoryId);
    }

    //新增内容
    public int insertContent(TbContent tbContent){
        return contentServiceApi.insertContent(tbContent);
    }

    //删除内容
    @Override
    public int deleteContent(long[] ids) {
        return contentServiceApi.deleteContent(ids);
    }

    //编辑内容
    @Override
    public int updateContent(TbContent tbContent) {
        return contentServiceApi.updateContent(tbContent);
    }
}
