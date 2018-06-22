package org.jframe.core.web;

/**
 * Created by leo on 2017-09-04.
 */
public class SelectItem {
    private String text;
    private String value;
    private boolean isSelected;

    public SelectItem(){

    }

    public SelectItem(String value, String text){
        this.value = value;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
