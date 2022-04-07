package com.xinlvyao.provider;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xinlvyao.api.TbItemParamServiceApi;
import com.xinlvyao.mapper.TbItemCatMapper;
import com.xinlvyao.mapper.TbItemParamMapper;
import com.xinlvyao.pojo.TbItemCat;
import com.xinlvyao.pojo.TbItemParam;
import com.xinlvyao.pojo.TbItemParamExample;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * 提供后台商品规格参数中的各个操作（全部商品规格参数，添加商品规格参数，
 * 删除商品规格参数）的TbItemParamServiceApi远程接口shixian
 */
@Service//注册到远程zookeeper远程服务注册中心
public class TbItemParamServiceApiImpl implements TbItemParamServiceApi {

    //注入TbItemParamMapper接口对象(商品规格参数)
    @Autowired
    private TbItemParamMapper tbItemParamMapper;

    //注入TbItemMapper接口对象（商品类目）
    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    //获取TbItemParam中数据总条数
    @Override
    public long selectCount() {
        return tbItemParamMapper.countByExample(null);
    }

    //获取TbItemParam中全部数据
    @Override
    public List<TbItemParam> selectAll(Integer page,Integer rows) {
        //设置分页：page页码,rows每页数据条数
        PageHelper.startPage(page,rows);
        //执行查询
        List<TbItemParam> tbItemParams = tbItemParamMapper.selectByExampleWithBLOBs(null);
        //遍历tbItemParams，根据itemCatId在tbItemCat表中查出其对应的商品类目名称
        for (TbItemParam tbItemParam:tbItemParams){
            //根据itemCatId在tbItemCat表中查出对应的商品类目名称
            TbItemCat tbItemCat = tbItemCatMapper.selectByPrimaryKey(tbItemParam.getItemCatId());
            //将商品类目名称赋值给TbItemParam中的ItemCatName属性
            tbItemParam.setItemCatName(tbItemCat.getName());
        }

        //执行分页
        List<TbItemParam> list = new PageInfo<>(tbItemParams).getList();

        return tbItemParams;
    }

    //根据itemCatId查询tbItemParam信息
    @Override
    public TbItemParam selectByItemCatId(long itemCatId) {
        //封装查询条件（itemCatId）
        TbItemParamExample itemParamExample = new TbItemParamExample();
        itemParamExample.createCriteria().andItemCatIdEqualTo(itemCatId);
        //执行查询
        List<TbItemParam> tbItemParams = tbItemParamMapper.selectByExampleWithBLOBs(itemParamExample);
        if (tbItemParams!=null&&tbItemParams.size()!=0){
            return tbItemParams.get(0);
        }
        return null;
    }

    //添加指定类目商品规格参数
    @Override
    @Transactional
    public int insertByItemCatId(TbItemParam tbItemParam) {
        return tbItemParamMapper.insert(tbItemParam);
    }

    //删除商品规格参数
    @Override
    @Transactional
    public int deleteById(long[] ids) {
        //遍历ids，根据id删除对应参数
        int n = 0;
        for (long id:ids){
            int i = tbItemParamMapper.deleteByPrimaryKey(id);
            n+=i;
        }
        if (n==ids.length){
            return 1;
        }
        return 0;
    }
}
