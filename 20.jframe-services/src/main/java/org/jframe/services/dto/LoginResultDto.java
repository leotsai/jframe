package org.jframe.services.dto;

import org.jframe.data.entities.OAuthWeixinUser;

/**
 * @author:qq
 * @date:2017/10/17
 */
public class LoginResultDto {
    private boolean isDidBindWeixin;
    private String nickname;
    private String imageUrl;

    public LoginResultDto() {
        this.isDidBindWeixin = false;
    }

    public LoginResultDto(OAuthWeixinUser user) {
        this.isDidBindWeixin = true;
        this.nickname = user.getNickname();
        this.imageUrl = user.getHeadimgUrl();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isDidBindWeixin() {
        return isDidBindWeixin;
    }

    public void setDidBindWeixin(boolean didBindWeixin) {
        isDidBindWeixin = didBindWeixin;
    }
}
