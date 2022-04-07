package com.xinlvyao.commons;

import java.util.List;

public class EasyUIDatagrid {
    private long total;    //查出来数据的总条数
    private List<?> list;  //查出来的结果

    public EasyUIDatagrid(long total, List<?> list) {
        this.total = total;
        this.list = list;
}

    public EasyUIDatagrid() {
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }
}
