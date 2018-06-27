package org.jframe.infrastructure.weixin;

import org.jframe.core.extensions.JList;

/**
 * Created by Leo on 2017/10/30.
 */
public enum QrCodePermanentType {
    UNKNOWN(0, "未知"),
    DEALER(1, "经销商专属二维码");

    public static final String Doc = "0：未知；1：经销商专属二维码";

    private int value;
    private String text;

    QrCodePermanentType(int value, String text){
        this.value = value;
        this.text = text;
    }

    public int getValue(){
        return this.value;
    }

    public String getText(){
        return this.text;
    }

    public static QrCodePermanentType from(Integer value) {
        QrCodePermanentType type = JList.from(QrCodePermanentType.values()).firstOrNull(x -> x.getValue() == value);
        return type == null ? QrCodePermanentType.UNKNOWN : type;
    }

}
