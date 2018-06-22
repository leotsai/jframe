package org.jframe.infrastructure.security;

import org.jframe.core.logging.LogHelper;
import org.jframe.infrastructure.AppContext;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;


/**
 * Created by Leo on 2017/1/8.
 */
public class Crypto {
    private static final String DesKey = "W5pK0o,";
    private static Cipher encryptCipher = null;
    private static Cipher decryptCipher = null;

    static {
        try {
            Security.addProvider(new com.sun.crypto.provider.SunJCE());
            Key key = getKey(DesKey.getBytes());

            encryptCipher = Cipher.getInstance("DES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);

            decryptCipher = Cipher.getInstance("DES");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception ex) {
            LogHelper.log("Crypto constructor", ex);
        }
    }

    /**
     * 老系统的MD5加密
     * @param input：未加密的密码
     * @param salt：member表的login_id
     * @return
     */
    public static String encryptMd5_old(String input, String salt) {
        input = input.toLowerCase();
        try {
            int middle = input.length() / 2;
            byte[] result = MessageDigest.getInstance("MD5").digest((input.substring(0, middle) + salt + input.substring(middle)).getBytes());
            StringBuilder strBuilder = new StringBuilder(result.length * 2);
            for (byte b : result) {
                String s = Integer.toHexString(b & 0x00FF);
                if (1 == s.length()) {
                    strBuilder.append('0');
                }
                strBuilder.append(s);
            }
            return strBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encryptWithSalt(String input, String salt) {
        return md5(input + salt);
    }

    public static String encrypt(String input) {
        return bytesToHex(encrypt(input.getBytes()));
    }

    public static String decrypt(String input) {
        return new String(decrypt(hexToBytes(input)));
    }

    /**
     * 返回值均为小写字母，不包含短横线“-”
     *
     * @param input
     * @return
     */
    public static String md5(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(input.getBytes());
            return bytesToHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            if (AppContext.getAppConfig().isTestServer()) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /***
     * 返回值均为小写字母，不包含短横线“-”
     * @param input
     * @return
     */
    public static String sha(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(input.getBytes());
            return bytesToHex(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            if (AppContext.getAppConfig().isTestServer()) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static String bytesToHex(byte[] bytes) {
        try {
            StringBuffer sb = new StringBuffer(bytes.length * 2);
            for (int i = 0; i < bytes.length; i++) {
                int intTmp = bytes[i];
                while (intTmp < 0) {
                    intTmp = intTmp + 256;
                }
                if (intTmp < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toString(intTmp, 16));
            }
            return sb.toString();
        } catch (Exception ex) {
            return null;
        }
    }

    private static byte[] hexToBytes(String hex) {
        try {
            byte[] bytes = hex.getBytes();
            int iLen = bytes.length;
            byte[] result = new byte[iLen / 2];
            for (int i = 0; i < iLen; i = i + 2) {
                String strTmp = new String(bytes, i, 2);
                result[i / 2] = (byte) Integer.parseInt(strTmp, 16);
            }
            return result;
        } catch (Exception ex) {
            return null;
        }
    }

    private static byte[] encrypt(byte[] bytes) {
        try {
            return encryptCipher.doFinal(bytes);
        } catch (Exception ex) {
            return null;
        }
    }

    private static byte[] decrypt(byte[] bytes) {
        try {
            return decryptCipher.doFinal(bytes);
        } catch (Exception ex) {
            return null;
        }
    }

    private static Key getKey(byte[] bytes) throws Exception {
        byte[] bytes8 = new byte[8];
        for (int i = 0; i < bytes.length && i < bytes8.length; i++) {
            bytes8[i] = bytes[i];
        }
        return new javax.crypto.spec.SecretKeySpec(bytes8, "DES");
    }

}
