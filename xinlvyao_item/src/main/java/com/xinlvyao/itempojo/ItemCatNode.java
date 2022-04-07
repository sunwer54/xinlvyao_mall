package com.xinlvyao.itempojo;

import java.io.Serializable;
import java.util.List;

/**
 * 该类用来封装data属性值（list集合）中的i属性的集合的元素
 * i属性的集合的元素有对象类型或者字符串类型，所以将i属性的
 * 类型定义为泛型集合
 */
public class ItemCatNode implements Serializable {
    private String u;
    private String n;
    private List<?> i;

    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u = u;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public List<?> getI() {
        return i;
    }

    public void setI(List<?> i) {
        this.i = i;
    }

    @Override
    public String toString() {
        return "ItemCatNode{" +
                "u='" + u + '\'' +
                ", n='" + n + '\'' +
                ", i=" + i +
                '}';
    }
}
