package org.jframe.services.flow;

import org.jframe.core.helpers.StringHelper;
import org.jframe.core.logging.LogHelper;
import org.jframe.core.weixin.core.dtos.UserDto;
import org.jframe.core.weixin.core.dtos.UserOAuthDto;
import org.jframe.data.JframeDbContext;
import org.jframe.data.entities.OAuthWeixinUser;
import org.jframe.data.enums.Gender;
import org.jframe.infrastructure.weixin.JframeWeixinApi;

import java.util.Date;

/**
 * @author:qq
 * @date:2017/11/23
 */
public class CreateWeixinUserIfNecessaryFlow {

    public void run(String openId, String accessToken) {
        try (JframeDbContext db = new JframeDbContext()) {
            if (db.getOAuthWeixinUserSet().getByOpenId(openId) != null) {
                return;
            }
            UserOAuthDto userInfo = JframeWeixinApi.getInstance().getUserInfo_OAuthCallback(openId, accessToken);
            if (userInfo == null || userInfo.isSuccess() == false) {
                LogHelper.log("weixin.getUserInfo2", userInfo == null ? "null" : userInfo.getErrmsg() + ":" + openId);
                return;
            }
            UserDto userDto = JframeWeixinApi.getInstance().getUserInfo(openId);
            boolean isSubscribed = false;
            Date subScribedTime = null;
            if (userDto != null && userDto.isSuccess() == true) {
                isSubscribed = userDto.isSubscribed();
                subScribedTime = isSubscribed ? new Date(userDto.getSubscribe_time()) : null;
            }

            OAuthWeixinUser weixinUser = new OAuthWeixinUser();
            weixinUser.setOpenId(userInfo.getOpenid());
            weixinUser.setNickname(StringHelper.cleanEmoji(userInfo.getNickname()));
            weixinUser.setGender(Gender.from(userInfo.getSex()));
            weixinUser.setProvince(userInfo.getProvince());
            weixinUser.setCity(userInfo.getCity());
            weixinUser.setCountry(userInfo.getCountry());
            weixinUser.setHeadimgUrl(userInfo.getHeadimgurl());
            weixinUser.setUnionId(userInfo.getUnionid());
            weixinUser.setSubscribed(isSubscribed);
            weixinUser.setSubscribeTime(subScribedTime);
            db.save(weixinUser);
            db.commitTransaction();
        }
    }

}
