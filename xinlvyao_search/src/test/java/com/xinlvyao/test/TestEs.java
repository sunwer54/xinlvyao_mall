package com.xinlvyao.test;

import com.xinlvyao.SearchApp;
import com.xinlvyao.mapper.TbItemMapper;
import com.xinlvyao.pojo.TbItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = SearchApp.class)
@RunWith(SpringRunner.class)
public class TestEs {
    @Autowired//注入Java中用来操作es的ElasticsearchRestTemplate类，子类中封装了用来操作es的方法
    private ElasticsearchRestTemplate restTemplate;
    /*
    根创建索引，设置映射
     */
    @Test
    public void createIndex(){
        //据pojo实体类的配置在es中创建索引（相当于创建数据库中创建表）
        //在pojo中指定了TbItem实体类用于ElasticSearch文档Document交互，所以可以直接传入TbItem的Class对象
        boolean isIndex = restTemplate.createIndex(TbItem.class);
        System.out.println("创建索引结果："+isIndex);

        //据pojo实体类的配置在es中创建创建mapping（相当于数据库中的表结构）
        boolean isMapping = restTemplate.putMapping(TbItem.class);
        System.out.println("创建mapping："+isMapping);
    }

    /**
     * 往es中批量插入数据,先从数据库中查询到全部数据，在批量插入到es中
     */
    //注入TbItemMapper接口对象（商品）
    @Autowired
    private TbItemMapper tbItemMapper;
    @Test
    public void insertDatas(){
        //1.从数据库中查询到数据
        List<TbItem> all = tbItemMapper.selectByExample(null);

        //2.把查询出来的数据放入es索引库中
        List<IndexQuery> indexQueries = new ArrayList<>();
        for(TbItem tbItem:all){
            //2.1把数据转化成 IndexQuery 对象
            IndexQuery indexQuery = new IndexQueryBuilder().withObject(tbItem).build();
            indexQueries.add(indexQuery);
        }
        //2.2指定批量插入数据到es中
        restTemplate.bulkIndex(indexQueries);
    }
}
