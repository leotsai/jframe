package org.jframe.data.enums;

import org.jframe.core.extensions.JList;

import java.util.Objects;

/**
 * Created by Leo on 2017/11/28.
 */
public enum DbCacheKey {
    UNKNOWN(0, "未知"),
    ROLE(1, "s_roles表"),
    SETTING(2,"s_settings表");

    public static final String Doc = "0：未知；1：s_roles表；2：s_settings表";

    private int value;
    private String text;

    DbCacheKey(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return this.value;
    }

    public String getText() {
        return this.text;
    }

    public static DbCacheKey from(Integer value) {
        DbCacheKey cacheKey = JList.from(DbCacheKey.values()).firstOrNull(x -> Objects.equals(x.getValue(), value));
        return cacheKey == null ? DbCacheKey.UNKNOWN : cacheKey;
    }
}
