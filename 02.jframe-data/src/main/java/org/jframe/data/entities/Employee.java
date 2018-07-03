package org.jframe.data.entities;

import org.jframe.data.converters.GenderConverter;
import org.jframe.data.core.EntityBase;
import org.jframe.data.enums.Gender;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Leo on 2018/1/16.
 */
@Entity
@Table(name = "s_employees")
public class Employee extends EntityBase {

    @Column(name = "user_id", columnDefinition = "bigint not null COMMENT '对应用户表的id列'")
    private Long userId;

    @Column(name = "phone", columnDefinition = "varchar(11) not null COMMENT '手机号'")
    private String phone;

    @Column(name = "name", columnDefinition = "varchar(50) not null COMMENT '真实姓名，设置时会同步到用户表'")
    private String name;

    @Column(name = "department_id", columnDefinition = "bigint not null COMMENT '部门ID'")
    private Long departmentId;

    @Column(name = "position", columnDefinition = "varchar(50) null COMMENT '职位'")
    private String position;

    @Column(name = "gender", columnDefinition = "int not null COMMENT '" + Gender.Doc + "'")
    @Convert(converter = GenderConverter.class)
    private Gender gender;

    @Column(name = "birthday", columnDefinition = "varchar(50) null COMMENT '生日'")
    private String birthday;

    @Column(name = "weixin", columnDefinition = "varchar(50) null COMMENT '微信号'")
    private String weixin;

    public void update(Employee employee){
        this.name = employee.getName();
        this.departmentId = employee.getDepartmentId();
        this.position = employee.getPosition();
        this.gender = employee.getGender();
        this.birthday = employee.getBirthday();
        this.weixin = employee.getWeixin();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
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
}
