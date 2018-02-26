package org.lychee.web.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lizhixiao
 * @date: 2018年2月26日
 * @Description:验证码工具类
 */
public class IdentifyingCodeUtil {
	final static String[] key = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "A", "B", "C", "D",
			"E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y",
			"Z" };
	static Random random = new Random();

	private static int width = 0;
	private static int height = 50;

	static BufferedImage image = null;

	/**
	 * 生成验证码字符
	 * @param length 验证码长度
	 * @return 验证码
	 */
	private static String genCodeStr(int length) {

		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buffer.append(key[random.nextInt(key.length)]);
		}
		return buffer.toString();
	}

	/**
	 * 生成验证码图片
	 * @param codeStr 验证码字符
	 * @return image
	 */
	private static BufferedImage drawImg(String codeStr) {
		width = 38 * codeStr.length();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		// 绘制背景
		// g.setColor(getRandomColor(200, 250));
		// 透明度设置
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1.0f));
		g.fillRect(0, 0, width, height);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));

		// 混淆线
		drawRandomLines(g, 1);
		// 混淆文字
		drawRandomText(g);
		// 干扰线
		drawCenterLine(g);

		StringBuffer strbuf = new StringBuffer();
		// 定义字体样式
		Font myFont = new Font("Consolas", Font.BOLD, 38);
		boolean b = random.nextBoolean();
		for (int i = 0; i < codeStr.length(); i++) {
			String temp = String.valueOf(codeStr.charAt(i));
			Color color = new Color(40 + random.nextInt(80), 40 + random.nextInt(80), 40 + random.nextInt(80));
			g.setColor(color);
			// 旋转一定的角度
			AffineTransform trans = new AffineTransform();
			int rad = random.nextInt(30);
			if (b = !b) {
				trans.rotate(Math.toRadians(rad), 50 * (i) + 8, 30);
			} else {
				trans.rotate(Math.toRadians(-rad), 50 * (i) + 8, 40);
			}
			// 缩放文字
			float scaleSize = random.nextFloat() + 0.8f;
			if (scaleSize > 1f) {
				scaleSize = 1f;
			}
			trans.scale(1f, 1f);
			g.setTransform(trans);
			// 写文字
			// 设置字体
			g.setFont(myFont);
			g.drawString(temp, 35 * i + (i == 0 ? 10 : 15), 40);
			strbuf.append(temp);
		}
		g.dispose();
		g.setBackground(Color.GREEN);
		return image;
	}

	/**
	 * 功能描述：随机颜色
	 */
	private static Color getRandomColor(int fc, int bc) {
		if (fc > 255)
			fc = 200;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 功能描述：混淆背景线
	 */
	private static void drawRandomLines(Graphics2D g, int nums) {
		int x1 = 0, y1 = 0;
		for (int i = 0; i < nums; i++) {
			g.setColor(getRandomColor(190, 230));
			int x2 = random.nextInt(width);
			int y2 = random.nextInt(height);
			g.drawLine(x1, y1, x2, y2);
			x1 = x2;
			y1 = y2;
		}
	}

	/**
	 * 功能描述：混淆文字
	 */
	private static void drawRandomText(Graphics2D g) {
		Font myFont = new Font("Consolas", Font.HANGING_BASELINE, 30);
		// 设置字体
		g.setFont(myFont);
		int max = 5 + random.nextInt(4);
		for (int i = 0; i < max; i++) {
			g.setColor(getRandomColor(170, 220));
			String temp = key[random.nextInt(key.length)];
			g.drawString(temp, i * 24, random.nextInt(height));
		}

	}

	/**
	 * 功能描述：中间混淆线
	 */
	private static void drawCenterLine(Graphics2D g) {
		Random random = new Random();
		int py = 3 + random.nextInt(6);
		int x1 = 0, y1 = height / 2, y2 = height / 2 - 1;
		boolean minus = true;
		for (int i = 0; i < width; i++) {
			g.setColor(getRandomColor(60, 80));
			g.drawLine(x1, y1, x1, y2);
			x1++;
			if (minus) {
				y1--;
				y2--;
			} else {
				y1++;
				y2++;
			}
			if (Math.abs(y1 - height / 2) > py) {
				minus = !minus;
				py = 3 + random.nextInt(6);
			}
		}
	}

	public static String genCodeStr(HttpServletResponse response) throws IOException {
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/png");
		String codeStr = IdentifyingCodeUtil.genCodeStr(4);
		IdentifyingCodeUtil.drawImg(codeStr);
		OutputStream os = response.getOutputStream();
		ImageIO.write(image, "JPEG", os);
		image.flush();
		if (os != null) {
			os.close();
		}
		return codeStr;
	}

}
