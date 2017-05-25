package org.jframe.data.entities;

import org.jframe.data.core.EntityBase;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Collection;

/**
 * Created by leo on 2017-05-25.
 */
@Entity(name = "users")
public class User extends EntityBase {

    @Column(nullable = false, length = 255)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 20)
    private String nickname;

    @Column(name = "image_key", columnDefinition = "varchar(100) null COMMENT 'user s avatar'")
    private String imageKey;

    @Column(name = "register_ip", columnDefinition = "varchar(20) null")
    private String registerIp;

    @Column(name = "login_times", columnDefinition = "int not null COMMENT 'increases on every login'")
    private int loginTimes;

    @Column(name = "last_login_ip", columnDefinition = "varchar(20) null")
    private String lastLoginIp;

    @Column(name = "last_login_time", columnDefinition = "datetime null")
    private String lastLoginTime;

    @Column(name = "is_disabled", columnDefinition = "bool not null")
    private boolean isDisabled;

    //------------------------------------------------------

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "user")
    private Collection<UserRoleRL> userRoleRLs;

    // below is our own code -------------------------------



    //-------------------------------


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    public int getLoginTimes() {
        return loginTimes;
    }

    public void setLoginTimes(int loginTimes) {
        this.loginTimes = loginTimes;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }


    public Collection<UserRoleRL> getUserRoleRLs() {
        return userRoleRLs;
    }

    public void setUserRoleRLs(Collection<UserRoleRL> userRoleRLs) {
        this.userRoleRLs = userRoleRLs;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
