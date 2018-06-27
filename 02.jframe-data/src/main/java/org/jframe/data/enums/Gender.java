package org.jframe.data.enums;

import org.jframe.core.extensions.JList;

import java.util.Objects;

/**
 * Created by leo on 2017-05-31.
 */
public enum Gender {
    UNKNOWN(0, "保密"),
    MALE(1, "男"),
    FEMALE(2, "女");

    public final static String Doc = "0: 保密; 1: 男; 2: 女";

    private final int value;
    private final String text;

    Gender(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return this.value;
    }

    public String getText() {
        return this.text;
    }

    public static Gender from(Integer value) {
        Gender gender = JList.from(Gender.values()).firstOrNull(x -> Objects.equals(x.getValue(), value));
        return gender == null ? Gender.UNKNOWN : gender;
    }

}
