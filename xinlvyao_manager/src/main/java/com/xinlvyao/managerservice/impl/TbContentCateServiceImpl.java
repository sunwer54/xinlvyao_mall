package com.xinlvyao.managerservice.impl;

import com.xinlvyao.api.TbContentCateServiceApi;
import com.xinlvyao.managerservice.TbContentCateService;
import com.xinlvyao.pojo.TbContentCategory;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;
import java.util.List;
/**
 * 提供后台内容分类管理中的各个操作（全部内容分类，新增内容分类，
 * 修改内容分类，删除内容分类）的TbItemContentCateService本地接口实现
 */
@Service
public class TbContentCateServiceImpl implements TbContentCateService {

    //从zookeeper远程服务注册中心订阅远程接口服务对象
    @Reference
    private TbContentCateServiceApi contentCateServiceApi;

    //获取全部内容分类
    @Override
    public List<TbContentCategory> selectAllContentCate(long parentId) {
        return contentCateServiceApi.selectAllContentCate(parentId);
    }

    //新增内容分类
    @Override
    public int insertContentCate(TbContentCategory tbContentCategory) {
        return contentCateServiceApi.insertContentCate(tbContentCategory);
    }

    @Override
    public int updateContentCateIsParent(long id, byte isParent) {
        return contentCateServiceApi.updateContentCateIsParent(id, isParent);
    }

    //根据id删除内容分类
    @Override
    public int deleteContentCate(long id) {
        return contentCateServiceApi.deleteContentCate(id);
    }

    //根据修改内容分类的名称
    @Override
    public int updateContentCateName(long id, String name) {
        return contentCateServiceApi.updateContentCateName(id, name);
    }

}
