package org.jframe.web.security;

import org.jframe.core.helpers.JsonHelper;
import org.jframe.core.helpers.StringHelper;
import org.jframe.infrastructure.helpers.CookieHelper;
import org.jframe.infrastructure.security.JframeCrypto;

/**
 * Created by Leo on 2017/1/9.
 */
public class WebIdentity {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String value) {
        username = value;
    }

    public void setPassword(String value) {
        password = value;
    }

    public WebIdentity() {

    }

    public WebIdentity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String toEncryptedRaw() {
        return JframeCrypto.getInstance().encrypt(JsonHelper.serialize(this));
    }

    public static WebIdentity parseFromRequest() {
        String cookie = CookieHelper.getAuthToken();
        if (cookie == null || StringHelper.isNullOrWhitespace(cookie)) {
            return null;
        }
        String decodedValue = JframeCrypto.getInstance().decrypt(cookie);
        return JsonHelper.deserialize(decodedValue, WebIdentity.class);
    }


}
