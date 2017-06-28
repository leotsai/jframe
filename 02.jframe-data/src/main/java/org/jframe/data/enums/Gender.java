package org.jframe.data.enums;

/**
 * Created by leo on 2017-05-31.
 */
public enum Gender {
    unknown(0),
    male(11),
    female(12);

    public final static String Doc = "0: unknown; 11: male; 12: female";

    private final int value;
    private Gender(int value){
        this.value = value;
    }

    public int getValue(){
        return this.value;
    }

}
