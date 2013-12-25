package org.springside.examples.quickstart.service.spider.util;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件名称为：URLAvailability.java 文件功能简述： 描述一个URL地址是否有效
 * 
 * @author Jason
 * @time 2010-9-14
 * 
 */
public class URLAvailability {
	private static URL url;
	private static HttpURLConnection con;
	private static int state = -1;

	/**
	 * 功能：检测当前URL是否可连接或是否有效, 描述：最多连接网络 5 次, 如果 5 次都不成功，视为该地址不可用
	 * 
	 * @param urlStr
	 *            指定URL网络地址
	 * @return URL
	 */
	public synchronized URL isConnect(String urlStr) {
		if (urlStr == null || urlStr.length() <= 0) {
			return null;
		}
		try {
			url = new URL(urlStr);
			con = (HttpURLConnection) url.openConnection();
			state = con.getResponseCode();
			if (state == 200) {
				System.out.println("URL可用！");
			}
		} catch (Exception ex) {
			System.out.println("URL不可用");
			url = null;
		}
		return url;
	}
}