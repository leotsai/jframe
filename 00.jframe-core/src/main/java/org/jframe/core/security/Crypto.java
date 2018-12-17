package org.jframe.core.security;

import org.jframe.core.logging.LogHelper;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;


/**
 * Created by Leo on 2017/1/8.
 */
public class Crypto {
    private Cipher encryptCipher;
    private Cipher decryptCipher;

    private final String key;
    private final String algorithm;

    public Crypto(String key, String algorithm) {
        this.key = key;
        this.algorithm = algorithm;
    }

    public void initialize() {
        try {
            Security.addProvider(new com.sun.crypto.provider.SunJCE());
            if (!"DES".equals(this.algorithm) && !"AES".equals(this.algorithm)) {
                throw new Exception("algorithm 参数仅支持DES和AES");
            }
            Key key = this.getKey(this.key.getBytes());
            encryptCipher = Cipher.getInstance(this.algorithm);
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);

            decryptCipher = Cipher.getInstance(this.algorithm);
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String encryptWithSalt(String input, String salt) {
        return md5(input+salt);
    }

    public String encrypt(String input){
        try{
            return bytesToHex(encrypt(input.getBytes()));
        }
        catch (Exception ex){
            LogHelper.error().log("__encrypt", ex);
            return null;
        }
    }

    public String decrypt(String input){
        try{
            return new String(decrypt(hexToBytes(input)));
        }
        catch (Exception ex){
            LogHelper.error().log("__decrypt", ex);
            return null;
        }
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
        } catch (NoSuchAlgorithmException ex) {
            LogHelper.error().log("__crypto.md5", ex);
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
        } catch (NoSuchAlgorithmException ex) {
            LogHelper.error().log("__crypto.sha", ex);
        }
        return null;
    }

    private static String bytesToHex(byte[] bytes) {
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
    }

    private static byte[] hexToBytes(String hex) {
        byte[] bytes = hex.getBytes();
        int iLen = bytes.length;
        byte[] result = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(bytes, i, 2);
            result[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return result;
    }

    private byte[] encrypt(byte[] bytes) throws Exception {
        return this.encryptCipher.doFinal(bytes);
    }

    private byte[] decrypt(byte[] bytes) throws Exception {
        return this.decryptCipher.doFinal(bytes);
    }

    private Key getKey(byte[] bytes) {
        byte[] bytes8 = new byte[8];
        for (int i = 0; i < bytes.length && i < bytes8.length; i++) {
            bytes8[i] = bytes[i];
        }
        return new javax.crypto.spec.SecretKeySpec(bytes8, this.algorithm);
    }

}
