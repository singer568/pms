package com.glodon.catchweb;

import java.net.*;

import java.io.*;

public class MockLogin {

	public static void main(String[] args) {
		try {
			String url = "http://biz.finance.sina.com.cn/suggest/lookup_n.php?q=600018&country=cn";
			String cookie = "";
			String data = "test!!!";

			HttpURLConnection.setFollowRedirects(true);

			HttpURLConnection urlConn = (HttpURLConnection) (new URL(url)
					.openConnection());
			urlConn.addRequestProperty("Cookie", cookie);
			urlConn.setRequestMethod("GET");
			urlConn.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows 2000)");

			urlConn.setDoOutput(true); // 需要向服务器写数据

			urlConn.setDoInput(true); //

			urlConn.setUseCaches(false); // 获得服务器最新的信息

			urlConn.setAllowUserInteraction(false);

			urlConn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			urlConn.setRequestProperty("Accept-Language", "zh-cn");

			urlConn.setRequestProperty("Cache-Control", "no-cache");

			urlConn.setRequestProperty("Pragma", "no-cache");

			urlConn.setRequestProperty("Host", "biz.finance.sina.com.cn");

			urlConn.setRequestProperty("Accept",
					"text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2");

			urlConn.setRequestProperty("Connection", "keep-alive");

			cookie = urlConn.getHeaderField("Set-Cookie");

			BufferedInputStream br = new BufferedInputStream(urlConn
					.getInputStream());

			FileOutputStream out = new FileOutputStream(new File("out.html"));

			int chByte = br.read();

			while (chByte != -1) {
				out.write(chByte);
				chByte = br.read();
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}