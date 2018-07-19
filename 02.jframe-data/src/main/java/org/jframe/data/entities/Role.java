package org.jframe.data.entities;

import org.jframe.core.extensions.JList;
import org.jframe.core.helpers.StringHelper;
import org.jframe.data.JframeDbContext;
import org.jframe.data.core.EntityBase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by leo on 2017-05-25.
 * 系统角色表
 */
@Entity
@Table(name = "s_roles")
public class Role extends EntityBase {
    @Column(name = "name", columnDefinition = "varchar(50) not null COMMENT '角色名称，具有唯一性'")
    private String name;

    @Column(name = "csv_codes", columnDefinition = "varchar(5000) null COMMENT '逗号分隔的权限编码，前后都有逗号'")
    private String csvCodes;

    @Column(name = "description", columnDefinition = "varchar(250) null COMMENT '该角色的备注信息'")
    private String description;

    @Column(name = "is_system", columnDefinition = "bool not null default 0 COMMENT '是否是系统角色，如果为否代表是自定义角色，系统角色不允许删除'")
    private boolean isSystem;

    public Role() {

    }

    private static Role createSystem(String name, String description) {
        Role role = new Role();
        role.name = name;
        role.csvCodes = null;
        role.description = description;
        role.isSystem = true;
        return role;
    }

    public void createRole(Role role) {
        this.name = role.getName();
        this.description = role.getDescription();
        this.csvCodes = role.getCsvCodes();
        this.isSystem = false;
    }

    public void updateRole(Role role) {
        this.name = role.getName();
        this.description = role.getDescription();
        this.csvCodes = role.getCsvCodes();
    }

    //==========================================================

    public static class Names {
        public static final String ADMIN = "ADMIN.NORMAL_ADMIN_ROLE";
        public static final String SUPER_ADMIN = "ADMIN.SUPER_ADMIN_ROLE";
    }

    public static void initSystemRoles() {
        JList<Role> systemRoles = new JList<>();
        systemRoles.add(createSystem(Names.ADMIN, "管理后台通用管理权限，即最低admin管理权限"));
        systemRoles.add(createSystem(Names.SUPER_ADMIN, "拥有整个系统的最高权限"));

        try (JframeDbContext db = new JframeDbContext()) {
            JList<Role> dbRoles = db.set(Role.class).getAll().where(x -> x.isSystem());
            JList<Role> added = systemRoles.where(x -> !dbRoles.any(d -> d.getName().equals(x.getName())));
            JList<Role> updated = dbRoles.where(d -> systemRoles.any(s -> s.getName().equals(d.getName()) && !d.getDescription().equals(s.getDescription())));
            added.forEach(x -> db.save(x));
            for (Role update : updated) {
                Role newRole = systemRoles.firstOrNull(x -> x.getName().equals(update.getName()));
                update.setDescription(newRole.getDescription());
                update.setCsvCodes(newRole.getCsvCodes());
                db.save(update);
            }
            String text = "    初始化系统角色，新增：" + added.size() + "，更新：" + updated.size();
            System.out.println(text);
        }
    }

    //==========================================================

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean system) {
        isSystem = system;
    }

    public JList<String> getCodes() {
        if (StringHelper.isNullOrWhitespace(this.csvCodes)) {
            return new JList<>();
        }
        return JList.splitByComma(this.csvCodes).where(x -> !StringHelper.isNullOrWhitespace(x));
    }

    public void setCodes(JList<String> codes) {
        this.csvCodes = "," + String.join(",", codes) + ",";
    }

    public String getCsvCodes() {
        return csvCodes;
    }

    public void setCsvCodes(String csvCodes) {
        this.csvCodes = csvCodes;
    }


    //--------------------------------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
