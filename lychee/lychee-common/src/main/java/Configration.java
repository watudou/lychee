
import org.lychee.common.encrypt.EncryptUtils;
import org.lychee.common.util.RandomUtil;

/**
 * Copyright (c) 2011-2014, hata (hatamail@qq.com).
 *
 */

/**
 * <b>configration</b><br>
 * 参数配置
 * 
 * @author lizhixiao
 * @date 2018年7月1日
 */
public class Configration {

	public static String AESKey;
	public static String DESKey;

	static {
		if (null == AESKey)
			AESKey = RandomUtil.getStringRandom(16, RandomUtil.mix);
		if (null == DESKey)
			DESKey = RandomUtil.getStringRandom(8, RandomUtil.mix);
	}

	public static void main(String[] args) {
		for (int i = 0; i < 4; i++) {

			String source = "1112";
			EncryptUtils encryptUtils = new EncryptUtils();
			try {
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

}
