package org.jframe.data.enums;

import org.jframe.core.extensions.JList;

import java.util.Objects;

/**
 * @author qq
 * @date 2018/7/11
 */
public enum SettingType {
    TEXT(1, "任意文本", false),
    JSON(2, "JSON格式文本", false),
    BOOLEAN(3, "布尔值", false),
    CHECKBOX(4, "复选", true),
    SELECT(5, "选择框", true);

    public static final String Doc = "1: 任意文本; 2: JSON格式文本; 3: 布尔值; 4: 复选; 5: 选择框";

    private final int value;
    private final String text;
    private final boolean fromSource;

    SettingType(int value, String text, boolean fromSource) {
        this.value = value;
        this.text = text;
        this.fromSource = fromSource;
    }

    public int getValue() {
        return this.value;
    }

    public String getText() {
        return this.text;
    }

    public static SettingType from(Integer value) {
        SettingType type = JList.from(SettingType.values()).firstOrNull(x -> Objects.equals(x.getValue(), value));
        return type == null ? SettingType.TEXT : type;
    }
}
