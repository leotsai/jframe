package org.jframe.data.entities;

import org.jframe.data.core.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * Created by Leo on 2018/1/12.
 */
@Entity
@Table(name = "s_permissions")
public class Permission extends EntityBase {

    @Column(name = "code", columnDefinition = "varchar(50) not null COMMENT '权限代码'")
    private String code;

    @Column(name = "name", columnDefinition = "varchar(50) null COMMENT '权限中文名称'")
    private String name;

    @Column(name = "`group`", columnDefinition = "varchar(50) not null COMMENT '分组名称'")
    private String group;

    @Column(name = "sub_group", columnDefinition = "varchar(50) null COMMENT '子分组名称'")
    private String subGroup;

    //================================================================


    //================================================================

    public Permission() {

    }

    public Permission(String group, String subGroup, String name, String code) {
        this.code = code;
        this.name = name;
        this.group = group;
        this.subGroup = subGroup;
    }


    public boolean isNameOrGroupChanged(Permission another) {
        return Objects.equals(this.getCode(), another.getCode())
                && (
                !Objects.equals(this.getName(), another.getName())
                        || !Objects.equals(this.getGroup(), another.getGroup())
                        || !Objects.equals(this.getSubGroup(), another.getSubGroup())
        );
    }

    //============================================================

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSubGroup() {
        return subGroup;
    }

    public void setSubGroup(String subGroup) {
        this.subGroup = subGroup;
    }

}
