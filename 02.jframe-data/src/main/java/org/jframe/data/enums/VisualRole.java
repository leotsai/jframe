package org.jframe.data.enums;

import org.jframe.core.extensions.JList;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Leo on 2018/1/3.
 */
public enum VisualRole {
    DEALER("DEALER", "dealer", null),
    DEALER_OWNER("DEALER-OWNER", "dealer-所有者", VisualRole.DEALER),
    PARTY_EMPLOYEE("PARTY-EMPLOYEE", "品鉴会审核者", null);

    private final String name;
    private final String text;
    private final VisualRole parent;

    VisualRole(String name, String text, VisualRole parent) {
        this.name = name;
        this.text = text;
        this.parent = parent;
    }

    public boolean isDealer() {
        return VisualRole.DEALER.equals(this) || VisualRole.DEALER.equals(this.parent);
    }

    //当前this = authorize.role = dealer_financial
    //roleName = dealer
    //应该：false

    //当前this = authorize.role = dealer
    //roleName = dealer_financial
    //应该：true
    public boolean meets(String roleName) {
        if (roleName.equals(this.name)) {
            return true;
        }
        return this.getChildren().any(x -> x.name.equals(roleName));
    }

    private static final ConcurrentHashMap<VisualRole, JList<VisualRole>> roleChildren = new ConcurrentHashMap<>();

    public JList<VisualRole> getChildren() {
        if (roleChildren.containsKey(this)) {
            return roleChildren.get(this);
        }
        JList<VisualRole> childRoles = JList.from(VisualRole.values()).where(x -> x.parent != null && x.parent == this);
        roleChildren.put(this, childRoles);
        return childRoles;
    }

    //================================================

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public VisualRole getParent() {
        return parent;
    }

}
