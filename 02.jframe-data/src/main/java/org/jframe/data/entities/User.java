package org.jframe.data.entities;

import org.jframe.core.extensions.KnownException;
import org.jframe.core.helpers.StringHelper;
import org.jframe.data.converters.GenderConverter;
import org.jframe.data.core.EntityBase;
import org.jframe.data.enums.Gender;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

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

    @Column(name = "gender", columnDefinition = "int not null COMMENT '" + Gender.Doc + "'")
    @Convert(converter = GenderConverter.class)
    private Gender gender;

    @Column(name = "image_key", columnDefinition = "varchar(100) null COMMENT 'user s avatar'")
    private String imageKey;

    @Column(name = "birth_year", columnDefinition = "int not null COMMENT ''")
    private int birthYear;

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

    // below is our own code -------------------------------

    public void validateOnRegister() throws KnownException {
        if (StringHelper.isNullOrWhitespace(this.username)) {
            throw new KnownException("please enter your email address as a username");
        }
        if (!StringHelper.isEmail(this.username)) {
            throw new KnownException("please enter a VALID email address as your username");
        }
        if (StringHelper.isNullOrWhitespace(this.password) || this.password.length() < 6 || this.password.length() > 20) {
            throw new KnownException("your password must be at least 6 chars and 20 chars at most");
        }
        if (StringHelper.isNullOrWhitespace(this.nickname) || this.nickname.length() < 2 || this.nickname.length() > 20) {
            throw new KnownException("please enter a valid nickname(1 - 20 chars)");
        }
        if (StringHelper.isNullOrWhitespace(this.imageKey)) {
            throw new KnownException("avartar is required");
        }
        if (this.gender == Gender.unknown) {
            throw new KnownException("please choose your gender");
        }
        if (this.birthYear > new Date().getYear() || this.birthYear < 1900) {
            throw new KnownException("invalid age");
        }
    }

    public User toRegisteringDefault(){
        User user = new User();
        user.setUsername(this.username);
        user.setPassword(this.password);
        user.setNickname(this.nickname);
        user.setImageKey(this.imageKey);
        return user;
    }


    //------------------------------------------------------

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "user")
    private Collection<UserRoleRL> userRoleRLs;





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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }
}
