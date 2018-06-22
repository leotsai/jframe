package org.jframe.core.weixin.core.dtos;

import org.jframe.core.extensions.JList;

/**
 * Created by leo on 2017-09-24.
 * https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140839
 */
public class UserDto extends _ApiDtoBase{
    private int subscribe;
    private String openid ;
    private String nickname ;
    private int sex ;//用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
    private String language ;
    private String province ;
    private String city;
    private String country ;
    private String headimgurl ;
    private long subscribe_time;
    private String unionid ;
    private String remark;
    private int groupid ;
    private Integer[] tagid_list;

    public boolean isSubscribed(){
        return this.subscribe == 1;
    }

    public String getCsvTagIds(){
        if(this.tagid_list == null || this.tagid_list.length == 0){
            return "";
        }
        return JList.from(this.tagid_list).joinString(",", x->x.toString());
    }

    //------------------------------------------------------

    public int getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(int subscribe) {
        this.subscribe = subscribe;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public long getSubscribe_time() {
        return subscribe_time;
    }

    public void setSubscribe_time(long subscribe_time) {
        this.subscribe_time = subscribe_time;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getGroupid() {
        return groupid;
    }

    public void setGroupid(int groupid) {
        this.groupid = groupid;
    }

    public Integer[] getTagid_list() {
        return tagid_list;
    }

    public void setTagid_list(Integer[] tagid_list) {
        this.tagid_list = tagid_list;
    }
}
