package com.xinlvyao.provider;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xinlvyao.api.TbContentServiceApi;
import com.xinlvyao.mapper.TbContentMapper;
import com.xinlvyao.pojo.TbContent;
import com.xinlvyao.pojo.TbContentExample;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * 提供后台内容管理中的各个操作（全部内容，新增内容，编辑内容，删除内容）的TbContentServiceApi远程接口实现
 */
@Service//注册到远程zookeeper远程服务注册中心
public class TbContentServiceApiImpl implements TbContentServiceApi {

    //注入TbContentMapper对象（内容）
    @Autowired
    private TbContentMapper contentMapper;

    //获取全部内容
    @Override
    public List<TbContent> selectAllContent(long categoryId,int page, int rows) {
        //分页：page页面, rows每页数据条数
        PageHelper.startPage(page, rows);
        //封装查询条件
        TbContentExample exp = new TbContentExample();
        exp.createCriteria().andCategoryIdEqualTo(categoryId);
        //执行查询
        List<TbContent> tbContents = contentMapper.selectByExampleWithBLOBs(exp);
        //执行分页
        return new PageInfo<>(tbContents).getList();
    }

    //获取内容的数据总数
    @Override
    public long selectCount(long categoryId) {
        //封装查询条件
        TbContentExample exp = new TbContentExample();
        exp.createCriteria().andCategoryIdEqualTo(categoryId);
        //执行查询
        return contentMapper.countByExample(exp);
    }

    //新增内容
    @Override
    @Transactional
    public int insertContent(TbContent tbContent){
        return contentMapper.insert(tbContent);
    }

    //删除内容
    @Override
    @Transactional
    public int deleteContent(long[] ids) {
        for (long id : ids){
            contentMapper.deleteByPrimaryKey(id);
        }
        return 1;
    }

    //编辑内容
    @Override
    @Transactional
    public int updateContent(TbContent tbContent) {
        return contentMapper.updateByPrimaryKeyWithBLOBs(tbContent);
    }
}
