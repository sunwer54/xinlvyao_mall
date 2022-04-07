package com.xinlvyao.itempojo;

import java.io.Serializable;
import java.util.List;

/**
 * 该类用来封装整体数据
 */
public class ItemCatAllNodeData implements Serializable {
    private List<Object> data;

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ItemCatAllNodeData{" +
                "data=" + data +
                '}';
    }
}
