package org.jframe.web.enums;

import org.jframe.core.extensions.JList;

/**
 * created by yezi on 2018/7/9
 */
public enum LogPattern {

    MONGODB, MYSQL, FILE;

    public static LogPattern convertLogPattern(String pattern) {
        return JList.from(LogPattern.values()).where(x -> x.equals(pattern)).firstOrNull();
    }
}
