package org.jframe.infrastructure.helpers;

import org.jframe.infrastructure.security.WpkCrypto;
import org.jframe.core.helpers.StringHelper;

/**
 * @author:qq
 * @date:2018/4/19
 */
public class AccessTokenHelper {

    public static String getPartyAccessToken(Long partyDealerId) {
        if (partyDealerId == null) {
            return null;
        }
        String uuid = StringHelper.newUuid();
        return WpkCrypto.getInstance().encrypt(uuid + partyDealerId);
    }

    public static Long parsePartyAccessToken(String token) {
        String decryptedToken = WpkCrypto.getInstance().decrypt(token);
        if (decryptedToken == null) {
            return null;
        }
        if (decryptedToken.length() <= 36) {
            return null;
        }
        return Long.valueOf(decryptedToken.substring(36));
    }
}
