package org.lychee.common.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密解密工具类
 */
public class EncryptUtils {
	/**
	 * 进行MD5加密
	 * 
	 * @param info要加密的信息
	 * @return String 加密后的字符串
	 */
	public String encryptToMD5(String info) {
		return encrypt(info, "MD5");
	}

	/**
	 * 进行MD5加密
	 * 
	 * @param info要加密的信息
	 * @salt 加密盐
	 * @return String 加密后的字符串
	 */
	public String encryptToMD5(String info, String salt) {
		info = mixed(info, salt);
		return encryptToMD5(info);
	}

	/**
	 * 进行SHA加密
	 * 
	 * @param info 要加密的信息
	 * @param type 要加密的类型
	 * @return String 加密后的字符串
	 */
	public String encrypt(String info, String type) {
		byte[] digesta = null;
		try {
			// 得到一个消息摘要
			MessageDigest alga = MessageDigest.getInstance(type);
			// 添加要进行计算摘要的信息
			alga.update(info.getBytes());
			// 得到该摘要
			digesta = alga.digest();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		// 将摘要转为字符串
		String rs = byte2hex(digesta);
		return rs;
	}

	/**
	 * 根据相应的加密算法、密钥、源文件进行加密，返回加密后的文件
	 * 
	 * @param Algorithm 加密算法:DES,AES
	 * @param key
	 * @param info
	 * @return
	 */
	public String encrypt(String Algorithm, SecretKey key, String info) {
		// 定义要生成的密文
		byte[] cipherByte = null;
		try {
			// 得到加密/解密器
			Cipher c1 = Cipher.getInstance(Algorithm);
			// 用指定的密钥和模式初始化Cipher对象
			// 参数:(ENCRYPT_MODE, DECRYPT_MODE, WRAP_MODE,UNWRAP_MODE)
			c1.init(Cipher.ENCRYPT_MODE, key);
			// 对要加密的内容进行编码处理,
			cipherByte = c1.doFinal(info.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 返回密文的十六进制形式
		return byte2hex(cipherByte);
	}

	/**
	 * 根据相应的解密算法、密钥和需要解密的文本进行解密，返回解密后的文本内容
	 * 
	 * @param Algorithm
	 * @param key
	 * @param sInfo
	 * @return
	 */
	public String decrypt(String Algorithm, SecretKey key, String sInfo) {
		byte[] cipherByte = null;
		try {
			// 得到加密/解密器
			Cipher c1 = Cipher.getInstance(Algorithm);
			// 用指定的密钥和模式初始化Cipher对象
			c1.init(Cipher.DECRYPT_MODE, key);
			// 对要解密的内容进行编码处理
			cipherByte = c1.doFinal(hex2byte(sInfo));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(cipherByte);
	}

	/**
	 * 根据相应的解密算法、指定的密钥和需要解密的文本进行解密，返回解密后的文本内容
	 * 
	 * @param Algorithm 加密算法:DES,AES
	 * @param key 这个key可以由用户自己指定 注意AES的长度为16位,DES的长度为8位
	 * @param sInfo
	 * @return
	 */
	public static String decrypt(String Algorithm, String sSrc, String sKey) throws Exception {
		try {
			// 判断Key是否正确
			if (sKey == null) {
				throw new Exception("Key为空null");
			}
			// 判断采用AES加解密方式的Key是否为16位
			if (Algorithm.equals("AES") && sKey.length() != 16) {
				throw new Exception("Key长度不是16位");
			}
			// 判断采用DES加解密方式的Key是否为8位
			if (Algorithm.equals("DES") && sKey.length() != 8) {
				throw new Exception("Key长度不是8位");
			}
			byte[] raw = sKey.getBytes("ASCII");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, Algorithm);
			Cipher cipher = Cipher.getInstance(Algorithm);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] encrypted1 = hex2byte(sSrc);
			try {
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original);
				return originalString;
			} catch (Exception e) {
				throw e;
			}
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * 根据相应的加密算法、指定的密钥、源文件进行加密，返回加密后的文件
	 * 
	 * @param Algorithm 加密算法:DES,AES
	 * @param key 这个key可以由用户自己指定 注意AES的长度为16位,DES的长度为8位
	 * @param info
	 * @return
	 */
	public static String encrypt(String Algorithm, String sSrc, String sKey) throws Exception {
		// 判断Key是否正确
		if (sKey == null) {
			throw new Exception("Key为空null");
		}
		// 判断采用AES加解密方式的Key是否为16位
		if (Algorithm.equals("AES") && sKey.length() != 16) {
			throw new Exception("Key长度不是16位");
		}
		// 判断采用DES加解密方式的Key是否为8位
		if (Algorithm.equals("DES") && sKey.length() != 8) {
			throw new Exception("Key长度不是8位");
		}
		byte[] raw = sKey.getBytes("ASCII");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, Algorithm);
		Cipher cipher = Cipher.getInstance(Algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(sSrc.getBytes());
		return byte2hex(encrypted);
	}

	/**
	 * 采用DES指定密钥的方式进行加密
	 * 
	 * @param key
	 * @param info
	 * @return
	 * @throws Exception
	 */
	public String encryptToDES(String key, String info) throws Exception {
		return encrypt("DES", info, key);
	}

	/**
	 * 采用DES用户指定密钥的方式进行解密，密钥需要与加密时指定的密钥一样
	 * 
	 * @param key
	 * @param sInfo
	 * @return
	 */
	public String decryptByDES(String key, String sInfo) throws Exception {
		return decrypt("DES", sInfo, key);
	}

	/**
	 * 采用AES指定密钥的方式进行加密
	 * 
	 * @param key
	 * @param info
	 * @return
	 * @throws Exception
	 */
	public String encryptToAES(String key, String info) throws Exception {
		return encrypt("AES", info, key);
	}

	public String decryptByECC(String key, String info) {
		return null;

	}

	/**
	 * 采用AES用户指定密钥的方式进行解密，密钥需要与加密时指定的密钥一样
	 * 
	 * @param key
	 * @param sInfo
	 * @return
	 */
	public String decryptByAES(String key, String sInfo) throws Exception {
		return decrypt("AES", sInfo, key);
	}

	/**
	 * 十六进制字符串转化为2进制
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hex2byte(String strhex) {
		if (strhex == null) {
			return null;
		}
		int l = strhex.length();
		if (l % 2 == 1) {
			return null;
		}
		byte[] b = new byte[l / 2];
		for (int i = 0; i != l / 2; i++) {
			b[i] = (byte) Integer.parseInt(strhex.substring(i * 2, i * 2 + 2), 16);
		}
		return b;
	}

	/**
	 * 将二进制转化为16进制字符串
	 * 
	 * @param b 二进制字节数组
	 * @return String
	 */
	private static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;
			}
		}
		return hs.toUpperCase();
	}

	/** 混淆 */
	private static String mixed(String str, String salt) {
		if (null == salt)
			return str;
		StringBuffer sb = new StringBuffer();
		int len = str.length();
		if (salt.length() < len) {
			len = salt.length();
		}
		for (int i = 0; i < len; i++) {
			sb.append(str.charAt(i)).append(salt.charAt(i));
		}
		if (salt.length() != str.length()) {
			sb.append(salt.length() > len ? salt.substring(len, salt.length()) : str.substring(len, str.length()));
		}
		return sb.toString();
	}

	/**
	 * 测试
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		EncryptUtils encryptUtils = new EncryptUtils();
		String source = "qqqq";
		System.out.println("Hello经过MD5:" + encryptUtils.encryptToMD5(source));

		System.out.println("========指定Key进行加解密==============");
		try {
			String AESKey = "";
			String DESKey = "";
			System.out.println(AESKey);
			System.out.println(DESKey);
			String str11 = encryptUtils.encryptToDES(DESKey, source);
			System.out.println("DES加密后为:" + str11);
			// 使用这个密匙解密
			String str12 = encryptUtils.decryptByDES(DESKey, str11);
			System.out.println("DES解密后为：" + str12);

			// 生成一个AES算法的密匙
			String strc = encryptUtils.encryptToAES(AESKey, source);
			System.out.println("AES加密后为:" + strc);
			// 使用这个密匙解密
			String strd = encryptUtils.decryptByAES(AESKey, strc);
			System.out.println("AES解密后为：" + strd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}