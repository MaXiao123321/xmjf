package com.shsxt.xmjf.web.demo.demo1;

/**
 * GeetestWeb配置文件
 * 
 *
 */
public class GeetestConfig {

	// 填入自己的captcha_id和private_key
	private static final String geetest_id = "d11f9119a68bf4bf1c1e4635bc10f05e";
	private static final String geetest_key = "2428bda10e45fe8fdeee971e3baa7f56";
	private static final boolean newfailback = true;

	public static final String getGeetest_id() {
		return geetest_id;
	}

	public static final String getGeetest_key() {
		return geetest_key;
	}
	
	public static final boolean isnewfailback() {
		return newfailback;
	}

}
