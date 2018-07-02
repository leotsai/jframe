package org.jframe.data.entities;

import org.jframe.core.helpers.HttpHelper;
import org.jframe.core.helpers.StringHelper;
import org.jframe.core.security.Crypto;
import org.jframe.data.converters.GenderConverter;
import org.jframe.data.core.EntityBase;
import org.jframe.data.enums.Gender;
import org.jframe.infrastructure.security.JframeCrypto;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Created by leo on 2017-05-25.
 * 系统用户表
 */
@Entity
@Table(name = "s_users", indexes = {
        @Index(name = "idx_username", columnList = "username", unique = true)
})
public class User extends EntityBase {

    @Column(name = "username", columnDefinition = "varchar(50) null COMMENT '登陆用户名，通常为手机号，允许空，如果为空表示未绑定手机号'")
    private String username;

    @Column(name = "password", columnDefinition = "varchar(100) null COMMENT '加密之后的密码，可空'")
    private String password;

    @Column(name = "password_salt", columnDefinition = "varchar(100) null COMMENT '密码加密的盐'")
    private String passwordSalt;

    @Column(name = "pay_password", columnDefinition = "varchar(100) null COMMENT '加密之后的支付密码，可空'")
    private String payPassword;

    @Column(name = "pay_password_salt", columnDefinition = "varchar(100) null COMMENT '支付密码加密的盐'")
    private String payPasswordSalt;

    @Column(name = "error_password_tries", columnDefinition = "int not null COMMENT '密码输入错误次数，当大于0时需要图形验证码，登陆成功后重置为0'")
    private int errorPasswordTries;

    @Column(name = "error_pay_password_tries", columnDefinition = "int not null COMMENT '支付密码输入错误次数，当大于设置的最大次数时，禁止输入，并提示用户在个人中心通过手机号重置支付密码'")
    private int errorPayPasswordTries;

    @Column(name = "nickname", columnDefinition = "varchar(20) not null COMMENT '用户昵称/姓名'")
    private String nickname;

    @Column(name = "gender", columnDefinition = "int not null COMMENT '" + Gender.Doc + "'")
    @Convert(converter = GenderConverter.class)
    private Gender gender;

    @Column(name = "image_key", columnDefinition = "varchar(100) null COMMENT '用户头像的key，用于从阿里云OSS读取图片'")
    private String imageKey;

    @Column(name = "register_ip", columnDefinition = "varchar(20) null COMMENT '注册IP地址'")
    private String registerIp;

    @Column(name = "login_times", columnDefinition = "int not null COMMENT '登陆次数'")
    private int loginTimes;

    @Column(name = "last_login_ip", columnDefinition = "varchar(20) null COMMENT '上次登陆IP地址'")
    private String lastLoginIp;

    @Column(name = "last_login_time", columnDefinition = "datetime null COMMENT '上次登陆时间'")
    private Date lastLoginTime;

    @Column(name = "birthday", columnDefinition = "datetime null COMMENT '生日，可空'")
    private Date birthday;

    @Column(name = "is_disabled", columnDefinition = "bool not null COMMENT '是否被禁用'")
    private boolean isDisabled;

    @Column(name = "is_logged_in", columnDefinition = "bool not null COMMENT '是否是登录状态,在注销登录时设为false'")
    private boolean isLoggedIn;

    @Column(name = "internal_note", columnDefinition = "varchar(255) null COMMENT '内部备注'")
    private String internalNote;

    @Column(name = "true_name", columnDefinition = "varchar(50) null COMMENT '真实姓名'")
    private String trueName;

    @Column(name = "is_subscribed", columnDefinition = "bool not null default false COMMENT '是否关注微信公众号，冗余oauthweixinuser表对应字段，两个字段同时维护'")
    private boolean isSubscribed;

    public User() {
        this.loginTimes = 0;
        this.gender = Gender.UNKNOWN;
        this.lastLoginTime = new Date();
        this.isDisabled = false;
        this.imageKey = Image.Keys.UserDefaultAvatar;
        this.isLoggedIn = false;
        this.isSubscribed = false;
    }

    public User(String username, String decryptedPassword) {
        this();
        this.username = username;
        this.nickname = StringHelper.hidePhoneNumber(username);
        this.passwordSalt = UUID.randomUUID().toString();
        this.password = Crypto.encryptWithSalt(decryptedPassword, this.passwordSalt);
    }

    public void markLoginSuccess() {
        this.setLastLoginIp(HttpHelper.getIp());
        this.setLastLoginTime(new Date());
        this.setLoginTimes(this.getLoginTimes() + 1);
        this.setErrorPasswordTries(0);
        this.isLoggedIn = true;
    }

    public void resetPassword(String decryptedPassword) {
        this.passwordSalt = UUID.randomUUID().toString();
        this.password = Crypto.encryptWithSalt(decryptedPassword, this.passwordSalt);
        this.errorPasswordTries = 0;
    }

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

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }

    public String getPayPasswordSalt() {
        return payPasswordSalt;
    }

    public void setPayPasswordSalt(String payPasswordSalt) {
        this.payPasswordSalt = payPasswordSalt;
    }

    public int getErrorPasswordTries() {
        return errorPasswordTries;
    }

    public void setErrorPasswordTries(int errorPasswordTries) {
        this.errorPasswordTries = errorPasswordTries;
    }

    public int getErrorPayPasswordTries() {
        return errorPayPasswordTries;
    }

    public void setErrorPayPasswordTries(int errorPayPasswordTries) {
        this.errorPayPasswordTries = errorPayPasswordTries;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public String getInternalNote() {
        return internalNote;
    }

    public void setInternalNote(String internalNote) {
        this.internalNote = internalNote;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }
}
