package org.jframe.service.admin.dto;

import org.jframe.core.extensions.*;
import org.jframe.core.hibernate.DtoEntity;
import org.jframe.data.JframeDbContext;
import org.jframe.data.entities.Role;
import org.jframe.data.enums.Gender;
import org.jframe.infrastructure.AppContext;
import org.jframe.service.admin.RoleService;


/**
 * created by yezi on 2018/1/23
 */
public class EmployeeDto implements DtoEntity, RankEntity {

    @Override
    public void setRank(int rank) {
        this.rank = rank;
    }

    private int rank;
    private Long id;
    private Long userId;
    private String phone;
    private String name;
    private String departmentName;
    private String position;
    private Gender gender;
    private String birthday;
    private String weixin;
    private JList<Role> roles;

    @Override
    public void fill(JMap<String, Object> row) {
        this.id = row.getLong("id");
        this.userId = row.getLong("user_id");
        this.phone = row.getString("phone");
        this.name = row.getString("name");
        this.departmentName = row.getString("department");
        this.position = row.getString("position");
        this.gender = Gender.from(row.getInteger("gender"));
        this.birthday = row.getString("birthday");
        this.weixin = row.getString("weixin");
        this.roles = AppContext.getBean(RoleService.class).getAllByUserId(userId);
    }

    public static PageResult<EmployeeDto> searchByDepartmentId(PageRequest request, JframeDbContext db, Long departmentId) {
        String sql = " select e.*,d.name as department from s_employees e left join s_departments d on e.department_id=d.id where department_id=:p0 order by id desc ";
        return db.getPage(EmployeeDto.class, sql, request, departmentId);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public JList<Role> getRoles() {
        return roles;
    }

    public void setRoles(JList<Role> roles) {
        this.roles = roles;
    }

    public int getRank() {
        return rank;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
