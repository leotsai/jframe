package org.jframe.core.weixin.core.helpers;

import org.jframe.core.helpers.JsonHelper;
import org.jframe.core.http.WebClient;
import org.jframe.core.logging.LogHelper;
import org.jframe.core.weixin.core.dtos._ApiDtoBase;

import java.io.File;

/**
 * Created by leo on 2017-09-23.
 */
public class WxHttpHelper {

    private static String getUrlPath(String fullUrl) {
        try {
            String url = fullUrl.substring(fullUrl.indexOf("//") + 2);
            url = url.substring(url.indexOf("/"));
            int lastIndex = url.indexOf("?");
            if (lastIndex >= 0) {
                url = url.substring(0, lastIndex);
            }
            return url.replace("/", "-");
        } catch (Exception ex) {
            LogHelper.error().log("InvalidWXUrl", fullUrl);
            return "invalid-url-format";
        }
    }

    public static String downloadString(String url){
        try(WebClient client = new WebClient())
        {
            return client.downloadString(url);
        }
        catch (Exception ex)
        {
            LogHelper.error().log("wx.httperror." + getUrlPath(url), ex);
            return null;
        }
    }

    public static <T extends _ApiDtoBase> T getApiDto(Class<T> clazz, String url)
    {
        String html;
        try(WebClient client = new WebClient())
        {
            html = client.downloadString(url);
        }
        catch (Exception ex)
        {
            LogHelper.error().log("wx.httperror." + getUrlPath(url), ex);
            return null;
        }
        return tryGetDto(clazz, html, url);
    }

    public static <T extends _ApiDtoBase> T postApiDto(Class<T> clazz, String url, String json)
    {
        String html;
        try (WebClient client = new WebClient())
        {
            html = client.post(url, json);
            LogHelper.error().log("wx.postFile result:", html);
        }
        catch (Exception ex){
            LogHelper.error().log("wx.httperror." + getUrlPath(url), ex);
            return null;
        }
        return tryGetDto(clazz, html, url);
    }

    public static <T extends _ApiDtoBase> T postFile(Class<T> clazz, String url, File file)
    {
        String html;
        try (WebClient client = new WebClient())
        {
            html = client.postFile(url,file);
        }
        catch (Exception ex){
            LogHelper.error().log("wx.httperror." + getUrlPath(url), ex);
            return null;
        }
        return tryGetDto(clazz, html, url);
    }

    private static <T extends _ApiDtoBase> T tryGetDto(Class<T> clazz, String html, String url) {
        try {
            T dto = JsonHelper.deserialize(html, clazz);
            if (dto.isSuccess() == false) {
                LogHelper.error().log("wx.failed." + getUrlPath(url), dto.getFullError());
            }
            return dto;
        } catch (Exception ex) {
            LogHelper.error().log("wx.jsonerror." + getUrlPath(url), ex);
            return null;
        }
    }


}
