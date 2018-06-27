package org.jframe.web.menu;


import org.jframe.core.extensions.JList;

/**
 * Created by Leo on 2018/1/17.
 */
public class MenuItem {
    private int order;
    private String id;
    private String text;
    private String icon;
    private String url;
    private MenuItem parent;
    private final JList<MenuItem> children = new JList<>();
    private final JList<String> permissions = new JList<>();

    public MenuItem() {

    }

    public MenuItem(String id, String text, String icon, String url) {
        this(0, id, text, icon, url);
    }

    public MenuItem(int order, String id, String text, String icon, String url) {
        this.order = order;
        this.id = id;
        this.text = text;
        this.icon = icon;
        this.url = url;
    }

    //apc: all permission codes (of the url)
    public void apc(String... codes) {
        for (String code : codes) {
            this.permissions.add(code);
        }
    }

    @Override
    public MenuItem clone() {
        return new MenuItem(this.id, this.text, this.icon, this.url);
    }

    //=================================================================

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public JList<MenuItem> getChildren() {
        return children;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JList<String> getPermissions() {
        return permissions;
    }

    public MenuItem getParent() {
        return parent;
    }

    public void setParent(MenuItem parent) {
        this.parent = parent;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
