package org.jframe.core.logging.enums;



import org.jframe.core.extensions.JList;

import java.util.Objects;

public enum LogArea {
    INFO(1, "调试、业务日志等"),
    ERROR(2, "错误类型"),
    FATAL(3, "严重错误，比如代码错误");

    public final static String DOC = "1: INFO, 2: ERROR, 3: FATAL";

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
        LogArea area = JList.from(LogArea.values()).firstOrNull(x -> Objects.equals(x.getValue(), value));
        return area == null ? LogArea.ERROR : area;
    }
}
