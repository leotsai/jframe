package org.jframe.service.demo.dto;

import org.jframe.core.extensions.JList;
import org.jframe.core.extensions.JMap;
import org.jframe.core.hibernate.DtoEntity;
import org.jframe.data.JframeDbContext;
import org.jframe.data.enums.Gender;
import org.jframe.infrastructure.AppContext;
import org.jframe.service.demo.EmployeeService;

/**
 * created by yezi on 2018/1/23
 */
public class EmployeeEditDto implements DtoEntity {

    private Long id;
    private Long userId;
    private String phone;
    private String name;
    private Integer departmentId;
    private String position;
    private Gender gender;
    private String birthday;
    private String weixin;
    private JMap<String, JMap<String, JList<String>>> employeePermissions;

    @Override
    public void fill(JMap<String, Object> row) {
        this.id = row.getLong("id");
        this.userId = row.getLong("user_id");
        this.phone = row.getString("phone");
        this.name = row.getString("name");
        this.departmentId = row.getInteger("department_id");
        this.position = row.getString("position");
        this.gender = Gender.from(row.getInteger("gender"));
        this.birthday = row.getString("birthday");
        this.weixin = row.getString("weixin");
    }

    public static EmployeeEditDto getByEmployeeId(JframeDbContext db, Long employeeId) {
        String sql = " select * from s_employees where id=:p0 ";
        EmployeeEditDto employeeEditDto = db.getFirst(sql, EmployeeEditDto.class, employeeId);
        employeeEditDto.setEmployeePermissions(AppContext.getBean(EmployeeService.class).getRolePermissonsByUserId(employeeEditDto.getUserId()));
        return employeeEditDto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
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

    public JMap<String, JMap<String, JList<String>>> getEmployeePermissions() {
        return employeePermissions;
    }

    public void setEmployeePermissions(JMap<String, JMap<String, JList<String>>> employeePermissions) {
        this.employeePermissions = employeePermissions;
    }
}
