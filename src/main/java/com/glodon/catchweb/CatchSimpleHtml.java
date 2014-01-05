package com.glodon.catchweb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.springside.examples.quickstart.entity.Subjects;
import org.springside.examples.quickstart.entity.Url;

public class CatchSimpleHtml {

	private static Logger logger = Logger.getLogger(CatchSimpleHtml.class
			.getName());

	private static HttpClient client = new HttpClient();

	public static final String PARAMETER = "parameter";

	public static void main(String[] args) {
		int currentNo = 0;

		try {
			CatchSimpleHtml catchHtml = new CatchSimpleHtml();
			catchHtml.catchPolicy(catchHtml.getUrl(),"2013-03-12");
		} catch (Exception e) {
			logger.debug("............................读取第" + currentNo
					+ "页内容出错.........................");
			e.printStackTrace();
		}
	}

	public Url getUrl() {
		Url url = new Url();
		url.setCode("001");
		url.setName("北京市");
		url
				.setUrl("http://zc.k8008.com/beijing/");
		url.setUrlPrefix("http://zc.k8008.com/beijing/");
		url
				.setSubjPath("//div[@class='list']/ul/li[parameter]/a");
		url
				.setLinkPath("//div[@class='list']/ul/li[parameter]/a");
		url
				.setDatePath("//div[@class='list']/ul/li[parameter]/span[1]");
		return url;
	}

	/**
	 * 取某天某网址的主题
	 * 一个网址产出一批主题
	 * @param url
	 *            给定网址
	 * @param catchDate
	 *            抓取日期
	 * @return
	 */
	public List<Subjects> catchPolicy(Url url, String catchDate) {
		List<Subjects> lst = null;
		try {
			TagNode html = getCleanedHtml(url.getUrl());

			String subjPath = url.getSubjPath();// 选取的主题的xpath
			String subjReplace = url.getSubjReplace();// 选取出主题后需要替换的字符串
			String datePath = url.getDatePath();// 发布日期的xpath
			String dateReplace = url.getDateReplace();// 选取出发布日期后需要替换掉的字符串
			String linkPath = url.getLinkPath();// 主题内部连接的xpath

			for (int i = 1; i < 20; i++) {
				String subjPathTmp = subjPath.replaceAll(PARAMETER, i + "");
				String datePathTmp = datePath.replaceAll(PARAMETER, i + "");
				String linkPathTmp = linkPath.replaceAll(PARAMETER, i + "");

				Object[] subjNode = html.evaluateXPath(subjPathTmp);
				Object[] dateNode = html.evaluateXPath(datePathTmp);
				Object[] linkNode = html.evaluateXPath(linkPathTmp);

				String subj = ((TagNode) subjNode[0]).getText().toString();
				
				String href = ((TagNode) linkNode[0])
						.getAttributeByName("href");
				String date = ((TagNode) dateNode[0]).getText().toString();
				System.out.println(subj);
				System.out.println(href);
				System.out.println(date);
//				subj = replaceSubj(subj, subjReplace);
//				date = replaceDate(date, dateReplace);
			}
		} catch (XPatherException e) {
			e.printStackTrace();
		}
		return lst;
	}

	private String replaceDate(String date, String dateReplace) {
		if (null != dateReplace) {
		}
		return date;
	}

	private String replaceSubj(String subj, String subjReplace) {

		return subj;
	}

	private TagNode getCleanedHtml(String url) {
		GetMethod getMethod = new GetMethod(url);
		TagNode node = null;
		try {
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
					new DefaultHttpMethodRetryHandler());
			int statusCode = client.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("method failed" + getMethod.getStatusLine());
			}
			// 读取内容
			byte[] responseBody = getMethod.getResponseBody();

			HtmlCleaner cleaner = new HtmlCleaner();
			CleanerProperties props = cleaner.getProperties();
			props.setAllowHtmlInsideAttributes(true);
			props.setAllowMultiWordAttributes(true);
			props.setRecognizeUnicodeChars(true);
			props.setOmitComments(true);

			String text = new String(responseBody);
			
			System.out.println(text);
			
			node = cleaner.clean(text);
		} catch (HttpException e) {
			// 发生致命异常，可能是协议不对或者返回的内容有问题
			System.out.println("Please check your provided http address");
			e.printStackTrace();
		} catch (IOException e) {
			// 发生网络异常
			e.printStackTrace();
		} finally {
			// 释放连接
			getMethod.releaseConnection();
		}
		return node;
	}

	/**
	 * 生成指定范围内的随机整数
	 * 
	 * @param max
	 *            最大整数
	 * @param min
	 *            最小整数
	 * @return
	 */
	public int randFloat(int max, int min) {
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}

	/**
	 * 设置等待时长
	 * 
	 * @param time
	 *            秒数
	 */
	public void wait(int time) {
		long now = System.currentTimeMillis();
		logger.debug("......................等待" + time
				+ "秒...........................");

		while (true) {
			long end = System.currentTimeMillis();

			if (end - now >= time * 1000) {
				break;
			}
		}

	}
}
