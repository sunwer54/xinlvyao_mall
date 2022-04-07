package com.xinlvyao.commons;

import java.io.Serializable;

/**
 * 轮播图大广告的数据封装类
 */
public class BigAd implements Serializable {
    /* 数据样式
    var data =
[{
	"srcB": "http://image.ego.com/images/2015/03/03/2015030304360302109345.jpg",
	"height": 240,
	"alt": "",
	"width": 670,
	"src": "http://image.ego.com/images/2015/03/03/2015030304360302109345.jpg",
	"widthB": 550,
	"href": "http://sale.jd.com/act/e0FMkuDhJz35CNt.html?cpdad=1DLSUE",
	"heightB": 240
}, {
	"srcB": "http://image.ego.com/images/2015/03/03/2015030304353109508500.jpg",
	"height": 240,
	"alt": "",
	"width": 670,
	"src": "http://image.ego.com/images/2015/03/03/2015030304353109508500.jpg",
	"widthB": 550,
	"href": "http://sale.jd.com/act/UMJaAPD2VIXkZn.html?cpdad=1DLSUE",
	"heightB": 240
}
     */
    //备份图片(页面没有实现)
    private String srcB;
    //图片的高
    private int height;
    private String alt;
    //图片的宽
    private int width;
    //图片的路径
    private String src;
    //备份图片的宽
    private int widthB;
    //点击图片要跳转的超链接
    private String href;
    //备份图片的高
    private int heightB;

    public BigAd() {
    }

    public String getSrcB() {
        return srcB;
    }

    public void setSrcB(String srcB) {
        this.srcB = srcB;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public int getWidthB() {
        return widthB;
    }

    public void setWidthB(int widthB) {
        this.widthB = widthB;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public int getHeightB() {
        return heightB;
    }

    public void setHeightB(int heightB) {
        this.heightB = heightB;
    }

    @Override
    public String toString() {
        return "BigAd{" +
                "srcB='" + srcB + '\'' +
                ", height=" + height +
                ", alt='" + alt + '\'' +
                ", width=" + width +
                ", src='" + src + '\'' +
                ", widthB=" + widthB +
                ", href='" + href + '\'' +
                ", heightB=" + heightB +
                '}';
    }
}
