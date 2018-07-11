package org.jframe.data.entities;

import org.jframe.data.converters.CaptchaUsageConverter;
import org.jframe.data.core.EntityBase;
import org.jframe.infrastructure.sms.CaptchaUsage;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by leo on 2017/5/15.
 */
@Entity
@Table(name = "s_captchas")
public class Captcha extends EntityBase {

    @Column(name = "`usage`", columnDefinition = "int not null COMMENT '使用该验证码的地方，枚举类型。" + CaptchaUsage.Doc + "'")
    @Convert(converter = CaptchaUsageConverter.class)
    private CaptchaUsage usage;

    @Column(name = "code", columnDefinition = "varchar(10) not null COMMENT '验证码'")
    private String code;

    @Column(name = "phone", columnDefinition = "varchar(20) not null COMMENT '手机号'")
    private String phone;

    @Column(name = "expire_time", columnDefinition = "datetime not null COMMENT '过期时间'")
    private Date expireTime;

    @Column(name = "tries", columnDefinition = "int not null COMMENT '尝试次数'")
    private int tries;

    @Column(name = "is_used", columnDefinition = "bool not null COMMENT '是否已被使用'")
    private boolean isUsed;

    @Column(name = "from_ip", columnDefinition = "varchar(20) null COMMENT '用户IP地址，用于防范过量调用'")
    private String fromIp;

    public Captcha(){
        this.usage = CaptchaUsage.GENERAL;
    }

    //-------------------------------

    public CaptchaUsage getUsage() {
        return usage;
    }

    public void setUsage(CaptchaUsage usage) {
        this.usage = usage;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public String getFromIp() {
        return fromIp;
    }

    public void setFromIp(String fromIp) {
        this.fromIp = fromIp;
    }

}
