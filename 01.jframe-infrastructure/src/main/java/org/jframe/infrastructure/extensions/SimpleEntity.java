package org.jframe.infrastructure.extensions;

/**
 * Created by screw on 2017/5/23.
 */
public class SimpleEntity<T> {
    private String id;
    private String name;
    private T value;

    public SimpleEntity(){

    }

    public SimpleEntity(String id, String name){
        this.id = id;
        this.name = name;
    }

    public SimpleEntity(String id, String name, T value){
        this(id, name);
        this.value = value;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
