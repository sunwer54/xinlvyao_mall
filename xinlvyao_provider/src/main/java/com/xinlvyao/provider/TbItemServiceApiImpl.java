package com.xinlvyao.provider;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xinlvyao.api.TbItemServiceApi;
import com.xinlvyao.mapper.TbItemCatMapper;
import com.xinlvyao.mapper.TbItemDescMapper;
import com.xinlvyao.mapper.TbItemMapper;
import com.xinlvyao.mapper.TbItemParamItemMapper;
import com.xinlvyao.pojo.*;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
/**
 * 提供后台全部商品中的各个操作（全部商品，新增商品，上架商品，下架商品，删除商品）的TbItemServiceApi接口实现
 */
@Service//注册到远程zookeeper远程服务注册中心
public class TbItemServiceApiImpl implements TbItemServiceApi {

    //注入TbItemMapper接口对象（商品）
    @Autowired
    private TbItemMapper tbItemMapper;

    //注入TbItemDescMapper接口对象（商品描述）
    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    //注入TbItemParamItemMapper接口对象（商品参数详情）
    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;

    //注入TbItemCatMapper接口对象（商品类目）
    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    //查询商品数据总条数
    @Override
    public long selectTbItemCount() {
        return tbItemMapper.countByExample(null);
    }

    //查询全部商品数据并使用分页
    @Override
    public List<TbItem> selectAllTbItem(int page, int rows) {
        //使用分页插件分页，page页码，rows每页数据条数
        PageHelper.startPage(page, rows);
        //添加按created字段降序
        TbItemExample exp = new TbItemExample();
        exp.setOrderByClause("created desc");
        //执行查询
        List<TbItem> tbItems = tbItemMapper.selectByExample(exp);
        //将查询的全部数据执行分页处理
        List<TbItem> list = new PageInfo<>(tbItems).getList();
        return list;
    }

    //下架商品
    @Override
    @Transactional(propagation= Propagation.REQUIRED)  //加入事务,默认的传播行为是REQUIRED
    public int downUpdateTbItem(long[] ids, int status, Date updated) {
        int n = 0;
        TbItem tbItem = new TbItem();
        //将数组中每条数据取出，逐个修改status
        for (long id:ids){
            tbItem.setStatus((byte)status);
            tbItem.setId(id);
            tbItem.setUpdated(updated);
            //根据主键修改
            int i = tbItemMapper.updateByPrimaryKeySelective(tbItem);
            n+=i;
        }
        return n;
    }

    //上架商品
    @Override
    @Transactional(propagation= Propagation.REQUIRED)  //加入事务,默认的传播行为是REQUIRED
    public int upUpdateTbItem(long[] ids, int status, Date updated) {
        int n = 0;
        TbItem tbItem = new TbItem();
        //将数组中每条数据取出，逐个修改status
        for (long id:ids){
            tbItem.setStatus((byte)status);
            tbItem.setId(id);
            tbItem.setUpdated(updated);
            //根据主键修改
            int i = tbItemMapper.updateByPrimaryKeySelective(tbItem);
            n+=i;
        }
        return n;
    }

    //删除商品
    @Override
    @Transactional(propagation= Propagation.REQUIRED)  //加入事务,默认的传播行为是REQUIRED
    public int deleteUpdateTbItem(long[] ids, int status, Date updated) {
        int n = 0;
        TbItem tbItem = new TbItem();
        //将数组中每条数据取出，逐个修改status
        for (long id:ids){
            tbItem.setStatus((byte)status);
            tbItem.setId(id);
            tbItem.setUpdated(updated);
            //根据主键修改
            int i = tbItemMapper.updateByPrimaryKeySelective(tbItem);
            n+=i;
        }
        return n;
    }

    //新增商品时的类目选择
    @Override
    public List<TbItemCat> selectCat(long parentId) {
        //封装查询条件(parentId)
        TbItemCatExample tbItemCatExample = new TbItemCatExample();
        tbItemCatExample.createCriteria().andParentIdEqualTo(parentId);
        //按查询条件(parentId)执行查询
        return tbItemCatMapper.selectByExample(tbItemCatExample);
    }

    //提交新增商品（提交商品数据到TbItem表，提交商品描述到TbItemDesc表,提交商品参数到TbItemParamItem表）
    @Override
    @Transactional(propagation= Propagation.REQUIRED)  //加入事务,默认的传播行为是REQUIRED
    public int insertProduct(TbItem tbItem,TbItemDesc tbItemDesc,TbItemParamItem tbItemParamItem){
        tbItemMapper.insert(tbItem);
        tbItemDescMapper.insert(tbItemDesc);
        tbItemParamItemMapper.insert(tbItemParamItem);
        return 1;
    }

    //根据id查询在TbItem表中查询商品信息
    @Override
    public TbItem selectItemById(long id) {
        return tbItemMapper.selectByPrimaryKey(id);
    }

    //根据itemId查询在TbItemDesc表中查询商品描述信息
    @Override
    public TbItemDesc selectItemDescByItemId(long itemId) {
        return tbItemDescMapper.selectByPrimaryKey(itemId);
    }

    //根据itemId查询在TbItemParamItem表中查询商品参数信息
    @Override
    public TbItemParamItem selectItemParamByItemId(long itemId) {
        TbItemParamItemExample exp = new TbItemParamItemExample();
        exp.createCriteria().andItemIdEqualTo(itemId);
        List<TbItemParamItem> tbItemParamItems = tbItemParamItemMapper.selectByExampleWithBLOBs(exp);
        if (tbItemParamItems.size()!=0&&tbItemParamItems!=null){
            return tbItemParamItems.get(0);
        }
        return null;
    }

    //修改数据库中对应商品的库存
    @Override
    @Transactional
    public int updateItemNum(long id,int num) {
        TbItem tbItem = new TbItem();
        tbItem.setId(id);
        tbItem.setNum(num);
        return tbItemMapper.updateByPrimaryKeySelective(tbItem);
    }
}
