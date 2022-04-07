package com.xinlvyao.provider;

import com.xinlvyao.api.TbContentCateServiceApi;
import com.xinlvyao.mapper.TbContentCategoryMapper;
import com.xinlvyao.pojo.TbContentCategory;
import com.xinlvyao.pojo.TbContentCategoryExample;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * 提供后台内容分类管理中的各个操作（全部内容分类，新增内容分类，
 * 修改内容分类，删除内容分类）的TbItemContentCateServiceApi远程接口实现
 */
@Service//注册到远程zookeeper远程服务注册中心
public class TbContentCateServiceApiImpl implements TbContentCateServiceApi {

    //注入TbContentCategoryMapper接口对象（内容分类）
    @Autowired
    private TbContentCategoryMapper categoryMapper;

    //获取全部内容分类
    @Override
    public List<TbContentCategory> selectAllContentCate(long parentId) {
        //TbContentCategoryExample对象封装查询条件
        TbContentCategoryExample exp = new TbContentCategoryExample();
        exp.createCriteria().andParentIdEqualTo(parentId);
        //执行查询
        return categoryMapper.selectByExample(exp);
    }

    //新增内容分类
    @Override
    @Transactional
    public int insertContentCate(TbContentCategory tbContentCategory) {
        return categoryMapper.insert(tbContentCategory);
    }

    //根据id修改分类的isParent为1
    @Override
    @Transactional
    public int updateContentCateIsParent(long id,byte isParent) {
        //封装修改条件和要修改为的数据
        TbContentCategory tbContentCategory = new TbContentCategory();
        tbContentCategory.setId(id);
        tbContentCategory.setIsParent(isParent);
        categoryMapper.updateByPrimaryKeySelective(tbContentCategory);
        return 1;
    }

    //根据id删除内容分类
    @Override
    @Transactional
    public int deleteContentCate(long id) {
        return categoryMapper.deleteByPrimaryKey(id);
    }

    //根据id修改内容分类的名称
    @Override
    @Transactional
    public int updateContentCateName(long id,String name) {
        //封装修改条件（id）和 分类的新名称（name）
        TbContentCategory tbContentCategory = new TbContentCategory();
        tbContentCategory.setId(id);
        tbContentCategory.setName(name);
        return categoryMapper.updateByPrimaryKeySelective(tbContentCategory);
    }

}
