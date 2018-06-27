package org.jframe.data.entities;

import org.jframe.data.core.EntityBase;
import org.jframe.data.enums.Gender;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by leo on 2017-08-28.
 * 微信OAuth用户信息表
 */
@Entity
@Table(name = "s_oauth_weixin_users", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id", unique = true),
        @Index(name = "idx_open_id", columnList = "open_id", unique = true)
})
public class OAuthWeixinUser extends EntityBase {

    @Column(name = "user_id", columnDefinition = "bigint null COMMENT '用户id'")
    private Long userId;

    @Column(name = "parent_user_id", columnDefinition = "bigint null COMMENT '邀请者用户ID'")
    private Long parentUserId;

    @Column(name = "open_id", columnDefinition = "varchar(100) not null COMMENT '微信open id，具有唯一性'")
    private String openId;

    @Column(name = "nickname", columnDefinition = "varchar(50) null COMMENT '用户微信昵称'")
    private String nickname;

    @Column(name = "gender", columnDefinition = "int not null COMMENT '" + Gender.Doc + "'")
    private Gender gender;

    @Column(name = "province", columnDefinition = "varchar(50) null COMMENT '用户所在省份'")
    private String province;

    @Column(name = "city", columnDefinition = "varchar(50) null COMMENT '用户所在城市'")
    private String city;

    @Column(name = "country", columnDefinition = "varchar(50) null COMMENT '用户所在国家'")
    private String country;

    @Column(name = "headimg_url", columnDefinition = "varchar(250) null COMMENT '用户头像的完整URL'")
    private String headimgUrl;

    @Column(name = "privileges", columnDefinition = "varchar(250) null COMMENT '用户特权集合，逗号分隔'")
    private String privileges;

    @Column(name = "union_id", columnDefinition = "varchar(100) null COMMENT '微信union id'")
    private String unionId;

    @Column(name = "is_subscribed", columnDefinition = "bool not null COMMENT '是否关注公众号'")
    private boolean isSubscribed;

    @Column(name = "subscribe_time", columnDefinition = "datetime null COMMENT '用户关注公众号的时间'")
    private Date subscribeTime;

    @Column(name = "remark", columnDefinition = "varchar(250) null COMMENT '备注'")
    private String remark;

    @Column(name = "group_id", columnDefinition = "int null COMMENT '用户分组ID'")
    private Integer groupId;

    @Column(name = "tag_ids", columnDefinition = "varchar(250) null COMMENT '用户标签集合，逗号分隔'")
    private String tagIds;

    public OAuthWeixinUser() {

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgUrl() {
        return headimgUrl;
    }

    public void setHeadimgUrl(String headimgUrl) {
        this.headimgUrl = headimgUrl;
    }

    public String getPrivileges() {
        return privileges;
    }

    public void setPrivileges(String privileges) {
        this.privileges = privileges;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public boolean isSubscribed() {
        return isSubscribed;
    }

    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public Date getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(Date subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public Long getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(Long parentUserId) {
        this.parentUserId = parentUserId;
    }
}
