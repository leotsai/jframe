package org.jframe.core.http;

/**
 * Created by leo on 2017-09-24.
 */
public class FormItem {
    private String name;
    private byte[] value;

    public FormItem(){

    }

    public FormItem(String name, byte[] value){
        this.name = name;
        this.value = value;
    }

    public FormItem(String name, String value) throws Exception{
        this.name = name;
        this.value = value == null ? null : value.getBytes("UTF-8");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getValue() {
        return value;
    }

    public void setValue(byte[] value) {
        this.value = value;
    }
}
