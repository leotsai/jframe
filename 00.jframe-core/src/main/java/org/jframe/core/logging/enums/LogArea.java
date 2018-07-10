package org.jframe.core.logging.enums;

import org.jframe.core.extensions.JList;

import java.util.Objects;

/**
 * created by yezi on 2018/7/9
 */
public enum LogArea {

    UNKNOWN(0, "未知类型"),
    ERROR(1, "error类型"),
    WARN(2, "warn类型");

    public final static String Doc = "0: 未知类型; 1: error类型; 2: warn类型";

    private final int value;
    private final String text;

    LogArea(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return this.value;
    }

    public String getText() {
        return this.text;
    }

    public static LogArea from(Integer value) {
        LogArea gender = JList.from(LogArea.values()).firstOrNull(x -> Objects.equals(x.getValue(), value));
        return gender == null ? LogArea.UNKNOWN : gender;
    }
}
