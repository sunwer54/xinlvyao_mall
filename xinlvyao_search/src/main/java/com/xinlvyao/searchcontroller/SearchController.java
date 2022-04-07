package com.xinlvyao.searchcontroller;

import com.google.gson.Gson;
import com.xinlvyao.pojo.TbItem;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class SearchController {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 从索引库es中搜索商品
     * @param q：搜索关键字
     * @param model：作用域对象，携带数据共享给jsp页面
     * @return
     */
    @RequestMapping("/search.html")
    public String searchProduct(String q, Model model){
        //根据条件在多字段的任意字段中搜索（title或者sellPoint）
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        List<QueryBuilder> should = boolQueryBuilder.should();
        //可在title字段中搜索
        should.add(QueryBuilders.matchQuery("title",q));
        //或者在sellPoint字段中搜索
        should.add(QueryBuilders.matchQuery("sellPoint",q));
        //加入搜索条件
        SearchQuery searchQueryShould = new NativeSearchQuery(boolQueryBuilder);
        //按照添加时间降序排序
        searchQueryShould.addSort(Sort.by(Sort.Direction.DESC,"created"));
        //执行搜索，得到搜索结果
        List<TbItem> tbItems = elasticsearchRestTemplate.queryForList(searchQueryShould, TbItem.class);
        //将搜索得到的结果放入model中，提供给jsp页面解析
        model.addAttribute("itemList",tbItems);
        return "search";
    }

    @RequestMapping(value = "/update/item/es",produces = "text/html;charset=utf-8")
    public void updateItemEs(@RequestBody String s){
        System.out.println(s);
        Gson gson = new Gson();
        TbItem tbItem = gson.fromJson(s, TbItem.class);
        IndexQuery indexQuery = new IndexQueryBuilder().withObject(tbItem).build();
        elasticsearchRestTemplate.index(indexQuery);
        System.out.println("es更新成功");
    }
}
