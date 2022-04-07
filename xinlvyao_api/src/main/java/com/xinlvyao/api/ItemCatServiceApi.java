package com.xinlvyao.api;
import com.xinlvyao.pojo.TbItemCat;
import java.util.List;

/**
 * 提供前台门户导航的远程服务接口
 */
public interface ItemCatServiceApi {
    public List<TbItemCat> selectAllItemCate(long parentId);
}
