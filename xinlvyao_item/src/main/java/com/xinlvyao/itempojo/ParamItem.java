package com.xinlvyao.itempojo;

import java.io.Serializable;
import java.util.List;

public class ParamItem implements Serializable {
	//对应tb_item_param_item表中的group分组信息
    private String group;
	//封装tb_item_param_item表中的params中的数据
    private List<Param> params;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<Param> getParams() {
        return params;
    }

    public void setParams(List<Param> params) {
        this.params = params;
    }
}
