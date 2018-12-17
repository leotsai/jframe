package org.jframe.core.helpers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jframe.core.exception.KnownException;
import org.jframe.core.logging.LogHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Created by screw on 2017/5/19.
 */
public class HttpHelper {

    private static final String PARSE_IP_LOCATION_URL = "http://ip.taobao.com/service/getIpInfo.php?ip={1}";
    private static final String PARSE_GEO_LOCATION_URL = "http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location={1}0&output=json&pois=1&ak=MSja28p4fX0GH2AjvsYC6oQ6QzOYdH8S";

    public static HttpServletRequest getCurrentRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception ex) {
            return null;
        }
    }

    public static HttpServletResponse getCurrentResponse() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 返回值如：http://www.5pinku.com
     *
     * @return
     */
    public static String getFullHost() {
        URI uri = URI.create(getCurrentRequest().getRequestURL().toString());
        return uri.getScheme() + "://" + uri.getAuthority();
    }

    public static String urlEncode(String url) {
        if (StringHelper.isNullOrWhitespace(url)) {
            return "";
        }
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (Exception ex) {
            LogHelper.error().log("__HttpHelper.urlEncode", ex);
            return "";
        }
    }

    public static String getPathAndQuery(HttpServletRequest request) {
        String url = request.getRequestURI();
        String param = request.getQueryString();
        return url + (param == null ? "" : "?" + param);
    }

    public static String getIp(HttpServletRequest request) {
        if (request == null) {
            return "0.0.0.0";
        }
        String ip = request.getHeader("X-Real-IP");
        if (StringHelper.eq(ip, "0.0.0.0")) {
            return "127.0.0.1";
        }
        return ip;
    }

    public static String getIp() {
        return getIp(getCurrentRequest());
    }


    public static JSONObject parseIpLocation(String ip) {
        try {
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<String> entity = restTemplate.getForEntity(PARSE_IP_LOCATION_URL, String.class, ip);
            if (HttpStatus.OK == entity.getStatusCode()) {
                String body = entity.getBody();
                JSONObject jsonObject = JSON.parseObject(body);
                Integer code = jsonObject.getInteger("code");
                if (0 != code) {
                    LogHelper.error().log("解析ip物理地址失败", entity.getBody());
                    return null;
                }
                return jsonObject.getJSONObject("data");
            }
        } catch (Exception e) {
            LogHelper.error().log("解析ip物理地址失败", e);
        }
        return null;
    }

    private static RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        // 支持中文编码
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        return restTemplate;
    }

    public static JSONObject parseGeoLocation(double lat, double lng) {
        try {
            if (lat < 0 || lat > 90 || lng < 0 || lng > 180) {
                throw new KnownException("参数异常");
            }
            StringBuilder sb = new StringBuilder();
            sb.append(lat).append(",").append(lng);
            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<String> entity = restTemplate.getForEntity(PARSE_GEO_LOCATION_URL, String.class, sb.toString());
            if (HttpStatus.OK == entity.getStatusCode()) {
                String body = entity.getBody();
                body = body.substring(29, body.length() - 1);
                JSONObject jsonObject = JSON.parseObject(body);
                Integer status = jsonObject.getInteger("status");
                if (0 != status) {
                    LogHelper.error().log("解析ip地理地址失败", entity.getBody());
                    return null;
                }
                //adcode
                return jsonObject.getJSONObject("result");
            }
        } catch (Exception e) {
            LogHelper.error().log("解析ip地理地址失败", e);
        }
        return null;
    }

}
