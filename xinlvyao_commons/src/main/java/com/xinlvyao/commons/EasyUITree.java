package com.xinlvyao.commons;
/**
 * 封装EasyUi需要的三个数据
 */

public class EasyUITree {
    private Long id;
    private String text;
    private String state;

    public EasyUITree() {
    }

    public EasyUITree(Long id, String text, String state) {
        this.id = id;
        this.text = text;   //节点的文本
        this.state = state; //1代表open,0代表closed
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
