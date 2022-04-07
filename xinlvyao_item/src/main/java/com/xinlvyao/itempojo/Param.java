package com.xinlvyao.itempojo;

import java.io.Serializable;

public class Param implements Serializable {
	//对应tb_item_param_item表中的params中的K
    private String k;
	//对应tb_item_param_item表中的params中的v
    private String v;

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }
}
