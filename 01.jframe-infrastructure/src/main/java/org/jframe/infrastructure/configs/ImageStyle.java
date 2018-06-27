package org.jframe.infrastructure.configs;

/**
 * Created by Leo on 2017/9/19.
 */
public enum ImageStyle {

    banner("banner"),//首页轮播图，900X500
    s60x60("s60x60"),// 60X60像素，用于后台上传控件
    full("full"),//原始尺寸，不进行压缩
    max1024("max-1024"),//原始尺寸，不进行压缩
    avartar50("avartar-50"),//头像，100X100
    s450x450("s450x450"),//用于商品详情轮播图，宽度900X900
    s80x80("s80x80"),//160x160
    s100x100("s100x100"),//200x200
    s120x120("s120x120"),//240x240
    s150x150("s150x150"),//300x300
    w450("w450");//用于商品详情，以及其他所有HTML页使用图片，最大宽度为900px

    private String style;

    ImageStyle(String style){
        this.style = style;
    }

    public String getStyle(){
        return this.style;
    }

}
