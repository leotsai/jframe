package org.jframe.core.weixin.core.parameters;

/**
 * @author xiaojin
 * @desc 生成小程序二维码参数
 * @date 2018-04-19 11:09
 */
public class AppletQrCodeParameter {

    private String page;

    private String scene;

    public AppletQrCodeParameter(String page, String scene) {
        this.page = page;
        this.scene = scene;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }
}
