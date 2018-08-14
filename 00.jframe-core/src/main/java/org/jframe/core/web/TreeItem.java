package org.jframe.core.web;

import org.jframe.core.extensions.JList;

/**
 * 树形数据结构
 */
public class TreeItem {

    private String value;
    private String text;
    private boolean selected = false;
    private JList<TreeItem> children;

    public TreeItem(String value, String text) {
        this(value, text, false);
    }

    public TreeItem(String value, String text, boolean selected) {
        this();
        this.value = value;
        this.text = text;
        this.selected = selected;
    }

    public TreeItem() {
        this.children = new JList<>();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public JList<TreeItem> getChildren() {
        return children;
    }

    public void setChildren(JList<TreeItem> children) {
        this.children = children;
    }
}
