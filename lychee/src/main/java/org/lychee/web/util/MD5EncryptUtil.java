package org.lychee.web.util;

import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;


/**
 * @author lizhixiao
 * @desc 加密工具
 */
public class MD5EncryptUtil {

    /**
     * 获取十六进制字符串形式的MD5摘要
     */
    public static String md5Hex(String src) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bs = md5.digest(src.getBytes());
            return new String(new Hex().encode(bs));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @desc 获取十六进制d大写字符串形式的MD5摘要
     */
    public static String md5Encrypt(String str) {
        try {
            MessageDigest lDigest = MessageDigest.getInstance("MD5");
            lDigest.update(str.getBytes());
            BigInteger lHashInt = new BigInteger(1, lDigest.digest());
            return String.format("%1$032X", lHashInt);
        } catch (NoSuchAlgorithmException lException) {
            throw new RuntimeException(lException);
        }
    }

    /**
     * md5加盐
     */
    public static String md5SaltEncrypt(String password) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        String salt = sb.toString();
        password = md5Hex(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return new String(cs);
    }

    /**
     * 校验密码是否正确
     */
    public static boolean md5Verify(String password, String md5) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5.charAt(i);
            cs1[i / 3 * 2 + 1] = md5.charAt(i + 2);
            cs2[i / 3] = md5.charAt(i + 1);
        }
        String salt = new String(cs2);
        return md5Hex(password + salt).equals(new String(cs1));
    }

    public static void main(String[] args) {
        String a = MD5EncryptUtil.md5Encrypt("123456");
        System.out.println(a);
    }
}
