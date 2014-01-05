package com.glodon.catchweb;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;

public class GetSource {

	public HttpClient client = new HttpClient();

	public String getSource(String url) {

		GetMethod method = new GetMethod(url);

		try {
			client.executeMethod(method);

		} catch (HttpException e) {

			// e.printStackTrace();

		} catch (IOException e) {

			// e.printStackTrace();

		}
		InputStream in = null;
		try {
			in = method.getResponseBodyAsStream();
		} catch (IOException e1) {

			// e1.printStackTrace();
		}
		in = new BufferedInputStream(in);
		Reader r = new InputStreamReader(in);
		int c;
		StringBuffer buffer = new StringBuffer();

		try {
			while ((c = r.read()) != -1)
				buffer.append((char) c);
		} catch (IOException e) {

			// e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {

			// e.printStackTrace();
		}
		method.releaseConnection();

		return buffer.toString();
	}

	private byte[] downBinaryFile(String strUrl2, Object object)
			throws IOException {
		System.out.println(strUrl2);
		URL cUrl = new URL(strUrl2);
		URLConnection uc = cUrl.openConnection();
		// String contentType = this.strType;
		int contentLength = uc.getContentLength();

		InputStream raw = uc.getInputStream();
		InputStream in = new BufferedInputStream(raw);
		byte[] data = new byte[contentLength];
		int bytesRead = 0;
		int offset = 0;
		while (offset < contentLength) {
			bytesRead = in.read(data, offset, data.length - offset);
			if (bytesRead == -1) {
				break;
			}
			offset += bytesRead;
		}
		in.close();
		return data;
	}
}