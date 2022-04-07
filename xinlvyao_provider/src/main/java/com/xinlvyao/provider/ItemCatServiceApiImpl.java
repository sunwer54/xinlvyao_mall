package com.xinlvyao.provider;

import com.xinlvyao.api.ItemCatServiceApi;
import com.xinlvyao.mapper.TbItemCatMapper;
import com.xinlvyao.pojo.TbItemCat;
import com.xinlvyao.pojo.TbItemCatExample;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
/**
 * 提供前台门户导航的远程服务接口的实现
 */
@Service//注册到远程zookeeper远程服务注册中心
public class ItemCatServiceApiImpl implements ItemCatServiceApi {

    //注入tbItemCatMapper对象（商品类目）
    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<TbItemCat> selectAllItemCate(long parentId) {
        //封装查询条件
        TbItemCatExample exp = new TbItemCatExample();
        exp.createCriteria().andParentIdEqualTo(parentId);
        //执行查询并返回查询结果
        return tbItemCatMapper.selectByExample(exp);
    }
}
