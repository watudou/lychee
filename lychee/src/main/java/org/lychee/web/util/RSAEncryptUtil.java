package org.lychee.web.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lizhixiao
 * @Date: 2019/10/10
 * @Description:
 */
public class RSAEncryptUtil {
    /**
     * 随机生成密钥对
     * Map<公钥,私钥>
     *
     * @throws NoSuchAlgorithmException
     */
    public static Map<String, String> genKeyPair() throws NoSuchAlgorithmException {
        Map<String, String> map = new HashMap<>();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        map.put(publicKeyString, privateKeyString);
        return map;
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    public static void main(String[] args) throws Exception {
        String str = "agUcIOUg/vTwHmnvUkWLJSpl2zqoUqrnqmXQhndPbtNmcx1aa1aVBLohIRQoF04lqU/hi0g+QXs0orgVy8t3j9oieiVTEvmZvoLMN8PhW89JmAONLXIL2Or+tQGAtJdcxxuujewqIgBlbkhZjFWcOZj2Bb/V1U4HULuDj+QKFFc=";
        String pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDbovKUAPo4ueOT+76iJrdGXsoCpl8BHNKdfE8mzUgbwUhYHgZPOyupiKEaops8Nx9skwTt4erfXf4+nsHnlNp4Z+X8kvhGDRoogsLD670EJ24doSZ5bdgvLa6SPlAxnTitZGzg9oe2hQH1rc5tWCte6tuGnrnUOKlsZ1042FBG2QIDAQAB";
        String priKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANui8pQA+ji545P7vqImt0ZeygKmXwEc0p18TybNSBvBSFgeBk87K6mIoRqimzw3H2yTBO3h6t9d/j6eweeU2nhn5fyS+EYNGiiCwsPrvQQnbh2hJnlt2C8trpI+UDGdOK1kbOD2h7aFAfWtzm1YK17q24aeudQ4qWxnXTjYUEbZAgMBAAECgYBwT1qLpBRPfX6J5WkNsY52RXI2+KwL/ZkVPsOaq837o1JC/NdtHGyvPFZlMFEA8Y74DhWvLp7TqVSg4L12lf9AL4pRsJBtxTdzYULGSi1Iy6RQKdtWjk04j1QH3mhMO8wNFyaJMyW77eEEizn4vqZBcr20000Y3JP9JjgCAi5uoQJBAP4fbEYRmffNfD2mgXTKq4LLPdT91LwCLyB+iwQyajyuovUBtLnDlSTAr+LZ4Q+vWnVCAUgRerfrA/QYqytoiiMCQQDdQk6VHqn2VjOwj2Yd0UMAkkieislpJPdWu17nnP6nD5CNdVCWHuOvmiw3Irf5SlUPHt8a3/3CpaTvSWErOKTTAkEAg8s+NzTHunnqtnqVZ0H5I3NO1Rjz7LHhSSP36yxOZrxXrWO+HB2wSwhX7/n5vE3AR1H9IihWke4j9cOZeoDUKwJAJ7MRBIKW/mMjLSfdq7XzbrPQodnHH72JP5+o/KfXrUQGrMsC5ZyvP7/K9S2ekvU6Y9cnMtxD3Nv5xxGEhMvKDwJBAKQNS4mrlFFfLCOu95j+RQnfFz+gyrn4CjLZIJPh9RhaY8vGnwrV3kBoGt3lywun2ZpKy4OwK3G5wzsiNZYwBJc=";
        String result = RSAEncryptUtil.encrypt("1111", pubKey);
//        String result = RSAEncryptUtil.decrypt(str, priKey);
        System.out.println(result);
    }

}
