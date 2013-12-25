package com.glodon.catchweb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.InputTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class CatchLoginHtml {

	private static Logger logger = Logger.getLogger(CatchLoginHtml.class.getName());

	
	/**
	 * 设定两个全局变量，每次读取到一个列表后就更新这两个字段
	 */
	private static String viewState = "";

	private static String eventValidation = "";

	private static String btn_x = "20";
	private static String btn_y = "10";

	private static Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();

	private static Map<Integer, List<Integer>> error = new HashMap<Integer, List<Integer>>();

	private static Map<String, List<String>> detailMap = new HashMap<String, List<String>>();

	public static void main(String[] args) {
		int currentNo = 0;

		try {
			
			logger.debug("..........................登录..........................");
			HttpClient client = login();

			wait(randFloat(5, 1));
			logger.debug("..........................取第1页内容..........................");
			fetchHomePage(client);

			for (int i = 2; i <= 123; i++) {// 从第二页到123页
				currentNo = i;
				logger.debug("............................将要读取第" + i
						+ "页的内容.........................");

				wait(randFloat(5, 1));

				fetchNextPage(i, client);
				logger.debug("............................第" + i
						+ "页读取完毕.........................");
			}
			logger.debug("............................................................完毕.............................................................成功&失败.......................");
			logger.debug(map);
			logger.debug(error);
			logger.debug("............................................................生成ID的excel....................................................................................");
			writeExcel(map);// 将生成的ID数组放到excel中

			logger.debug("............................................................取详细信息....................................................................................");
			fetchDetailPage(client, map);
			logger.debug("............................................................完毕...........写入excel.........................................................................");
			writeExcelAll(map, detailMap);
			logger.debug("............................................................全部完毕....................................................................................");
		} catch (Exception e) {
			logger.debug("............................读取第" + currentNo
					+ "页内容出错.........................");
			e.printStackTrace();
		}

	}

	public static void fetchDetailPage(HttpClient client,
			Map<Integer, List<String>> map) throws Exception {

		Set<Map.Entry<Integer, List<String>>> entrySet = map.entrySet();
		Iterator<Map.Entry<Integer, List<String>>> it = entrySet.iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, List<String>> entry = it.next();
			List<String> lst = entry.getValue();
			logger.debug("................取【"+ entry.getKey() +"】页................");

			for (int i = 0; i < lst.size(); i++) {
				logger.debug("................取【"+ entry.getKey() +"】页的第【" + lst.get(i) + "】. "+i +"...............");
				wait(randFloat(3, 1));
				GetMethod getMethod = new GetMethod(
						"http://www.catarc.org.cn/standard/StandardDetail.aspx?standardid="
								+ lst.get(i));
				client.executeMethod(getMethod);
				String text = getMethod.getResponseBodyAsString();
				addDetailToMap(lst.get(i), text);
			}

		}
	}

	private static void addDetailToMap(String key, String text)
			throws Exception {
		Parser parser = Parser.createParser(text, "gb2312");
		TagNameFilter tableFiler = new TagNameFilter("table");

		NodeList nodes = parser.parse(tableFiler);

		TableTag node = (TableTag) nodes.elementAt(5);

		TableRow[] rows = node.getRows();
		for (int i = 1; i < 11; i++) {
			TableColumn[] cols = rows[i].getColumns();

			StringBuffer txt1 = new StringBuffer();

			StringBuffer txt2 = new StringBuffer();

			NodeList span1 = cols[1].getChildren().elementAt(1).getChildren();

			for (int j = 0; j < span1.size(); j++) {
				if (span1.elementAt(j) instanceof TextNode) {
					txt1.append(span1.elementAt(j).getText()).append(" ");
				}
			}

			NodeList span2 = cols[3].getChildren().elementAt(1).getChildren();

			for (int j = 0; j < span2.size(); j++) {
				if (span2.elementAt(j) instanceof TextNode) {
					txt2.append(span2.elementAt(j).getText()).append(" ");
				}
			}

			List<String> lst = detailMap.get(key);
			if (lst == null) {
				lst = new ArrayList<String>();
			}
			lst.add(txt1.toString().trim());
			lst.add(txt2.toString().trim());
			detailMap.put(key, lst);
		}

	}

	public static void writeExcelAll(Map<Integer, List<String>> map,
			Map<String, List<String>> detailMap) throws Exception {
		WritableWorkbook book = Workbook.createWorkbook(new File(
				"D:\\Details.xls"));
		WritableSheet sheet1 = book.createSheet("IDS", 0);
		sheet1.addCell(new Label(0, 0, "页码"));
		sheet1.addCell(new Label(1, 0, "主键"));
		if (null != detailMap) {
			sheet1.addCell(new Label(2, 0, "标准号"));
			sheet1.addCell(new Label(3, 0, "状态"));
			sheet1.addCell(new Label(4, 0, "中文名称"));
			sheet1.addCell(new Label(5, 0, "英文名称"));
			sheet1.addCell(new Label(6, 0, "所属类别"));
			sheet1.addCell(new Label(7, 0, "标准属性"));
			sheet1.addCell(new Label(8, 0, "发布日期"));
			sheet1.addCell(new Label(9, 0, "实施日期"));
			sheet1.addCell(new Label(10, 0, "提出部门"));
			sheet1.addCell(new Label(11, 0, "归口单位"));
			sheet1.addCell(new Label(12, 0, "起草单位"));
			sheet1.addCell(new Label(13, 0, "历次版本 "));
			sheet1.addCell(new Label(14, 0, "起草人"));
			sheet1.addCell(new Label(15, 0, "代替标准"));
			sheet1.addCell(new Label(16, 0, "ICS"));
			sheet1.addCell(new Label(17, 0, "CCS"));
			sheet1.addCell(new Label(18, 0, "采标程度"));
			sheet1.addCell(new Label(19, 0, "采标号"));
			sheet1.addCell(new Label(20, 0, "页数"));
			sheet1.addCell(new Label(21, 0, "定价(元)"));
		}

		int row = 1; // 表示从第一行开始
		Set<Map.Entry<Integer, List<String>>> entrySet = map.entrySet();
		Iterator<Map.Entry<Integer, List<String>>> it = entrySet.iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, List<String>> entry = it.next();
			Integer key = entry.getKey();
			List<String> lst = entry.getValue();
			for (int i = 0; i < lst.size(); i++) {
				sheet1.addCell(new Label(0, row, key + ""));
				sheet1.addCell(new Label(1, row, lst.get(i)));
				if (null != detailMap) {
					List<String> detailLst = detailMap.get(lst.get(i));
					sheet1.addCell(new Label(2, row, detailLst.get(0)));
					sheet1.addCell(new Label(3, row, detailLst.get(1)));
					sheet1.addCell(new Label(4, row, detailLst.get(2)));
					sheet1.addCell(new Label(5, row, detailLst.get(3)));
					sheet1.addCell(new Label(6, row, detailLst.get(4)));
					sheet1.addCell(new Label(7, row, detailLst.get(5)));
					sheet1.addCell(new Label(8, row, detailLst.get(6)));
					sheet1.addCell(new Label(9, row, detailLst.get(7)));
					sheet1.addCell(new Label(10, row, detailLst.get(8)));
					sheet1.addCell(new Label(11, row, detailLst.get(9)));
					sheet1.addCell(new Label(12, row, detailLst.get(10)));
					sheet1.addCell(new Label(13, row, detailLst.get(11)));
					sheet1.addCell(new Label(14, row, detailLst.get(12)));
					sheet1.addCell(new Label(15, row, detailLst.get(13)));
					sheet1.addCell(new Label(16, row, detailLst.get(14)));
					sheet1.addCell(new Label(17, row, detailLst.get(15)));
					sheet1.addCell(new Label(18, row, detailLst.get(16)));
					sheet1.addCell(new Label(19, row, detailLst.get(17)));
					sheet1.addCell(new Label(20, row, detailLst.get(18)));
					sheet1.addCell(new Label(21, row, detailLst.get(19)));
				}

				++row;
			}
		}
		// 写入数据并关闭文件
		book.write();
		book.close();
		logger.debug("生成excel文件成功");
	}

	public static void writeExcel(Map<Integer, List<String>> map)
			throws Exception {
		WritableWorkbook book = Workbook
				.createWorkbook(new File("D:\\IDS.xls"));
		WritableSheet sheet1 = book.createSheet("IDS", 0);
		sheet1.addCell(new Label(0, 0, "页码"));
		sheet1.addCell(new Label(1, 0, "主键"));

		int row = 1; // 表示从第一行开始
		Set<Map.Entry<Integer, List<String>>> entrySet = map.entrySet();
		Iterator<Map.Entry<Integer, List<String>>> it = entrySet.iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, List<String>> entry = it.next();
			Integer key = entry.getKey();
			List<String> lst = entry.getValue();
			for (int i = 0; i < lst.size(); i++) {
				sheet1.addCell(new Label(0, row, key + ""));
				sheet1.addCell(new Label(1, row, lst.get(i)));
				++row;
			}
		}
		// 写入数据并关闭文件
		book.write();
		book.close();
		logger.debug("生成excel文件成功");
	}

	/**
	 * 登录
	 * 
	 * @return
	 * @throws IOException
	 * @throws HttpException
	 */
	public static HttpClient login() throws IOException, HttpException {
		HttpClient client = new HttpClient();
		client.getHostConfiguration().setHost("www.catarc.org.cn");
		client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		NameValuePair[] data = {
				new NameValuePair("User-Agent",
						"Mozilla/5.0 (Windows NT 5.1; rv:25.0) Gecko/20100101 Firefox/25.0"),
				new NameValuePair("__VIEWSTATE",
						"/wEPDwUJNzE2NjczNjA4ZGQCaV1Dt0DOUg5r7O3ZD+sGL1Hq6Q=="),
				new NameValuePair("__EVENTVALIDATION",
						"/wEWBQK/2uyqBgKl1bKzCQK1qbSRCwLZpaCZAwKTuvSuCTYE9T8nbPNCPEY2eqmCy5I3BwtG"),
				new NameValuePair("txtUserName", "asri"),
				new NameValuePair("txtPassword", "asri"),
				new NameValuePair("loginBtn", "%B5%C7%C2%BD") };
		PostMethod post = new PostMethod(
				"http://www.catarc.org.cn/standard/Default.aspx");
		post.setRequestBody(data);
		client.executeMethod(post);

		return client;
	}

	/**
	 * 获取主页列表
	 * 
	 * @param client
	 * @throws IOException
	 * @throws HttpException
	 */
	public static void fetchHomePage(HttpClient client) throws Exception {
		// 获得登陆后的 Cookie
		Cookie[] cookies = client.getState().getCookies();
		String tmpcookies = "";
		for (Cookie c : cookies) {
			tmpcookies += c.toString() + ";";
		}
		// 进行登陆后的操作
		GetMethod getMethod = new GetMethod(
				"http://www.catarc.org.cn/standard/SearchStandard.aspx");
		// 每次访问需授权的网址时需带上前面的 cookie 作为通行证
		getMethod.setRequestHeader("cookie", tmpcookies);
		client.executeMethod(getMethod);
		String text = getMethod.getResponseBodyAsString();

		setViewState(text);

		setEventValidation(text);

		setStandardIdsToMap(1, text);

	}

	private static void setStandardIdsToMap(Integer pageNo, String html)
			throws Exception {
		Parser parser = Parser.createParser(html, "gb2312");
		AndFilter viewStateFilter = new AndFilter(new TagNameFilter("table"),
				new HasAttributeFilter("id",
						"ctl00_ContentPlaceHolder1_StandardView"));

		NodeList nodes = parser.parse(viewStateFilter);
		TableTag node = (TableTag) nodes.elementAt(0);

		TableRow[] rows = node.getRows();
		for (int i = 1; i < rows.length; i++) {
			TableColumn[] cols = rows[i].getColumns();
			TableColumn col = cols[3];
			LinkTag tag = (LinkTag) ((Div) col.getChildren().elementAt(1))
					.getChildren().elementAt(2);
			if (tag == null) {
				List<Integer> lst = error.get(pageNo);
				if (lst == null) {
					lst = new ArrayList<Integer>();
				}
				lst.add(i);
				error.put(pageNo, lst);
				continue;
			}

			String href = tag.getAttribute("href");
			if (href == null) {
				List<Integer> lst = error.get(pageNo);
				if (lst == null) {
					lst = new ArrayList<Integer>();
				}
				lst.add(i);
				error.put(pageNo, lst);
				continue;
			}
			int start = href.indexOf("standardid=");
			int end = href.indexOf("&amp;");

			String standardId = href.substring(start, end).replaceAll(
					"standardid=", "");

			List<String> lst = map.get(pageNo);
			if (lst == null) {
				lst = new ArrayList<String>();
			}
			lst.add(standardId);
			map.put(pageNo, lst);
		}

	}

	public static void fetchNextPage(Integer pageNo, HttpClient client)
			throws Exception {
		NameValuePair[] data = {
				new NameValuePair("User-Agent",
						"Mozilla/5.0 (Windows NT 5.1; rv:25.0) Gecko/20100101 Firefox/25.0"),

				new NameValuePair("__EVENTARGUMENT", ""),
				new NameValuePair("__EVENTTARGET", ""),

				new NameValuePair("__VIEWSTATE", viewState),

				new NameValuePair("__EVENTVALIDATION", eventValidation),
				new NameValuePair(
						"ctl00%24ContentPlaceHolder1%24ImplementDayDDList", "0"),
				new NameValuePair(
						"ctl00%24ContentPlaceHolder1%24ImplementMonthDDList",
						"0"),
				new NameValuePair("ctl00%24ContentPlaceHolder1%24PropertyDDL",
						"2"),
				new NameValuePair(
						"ctl00%24ContentPlaceHolder1%24PublishedDayDDList", "0"),
				new NameValuePair(
						"ctl00%24ContentPlaceHolder1%24PublishedMonthDDList",
						"0"),
				new NameValuePair("ctl00$ContentPlaceHolder1$btnNext.x", btn_x),
				new NameValuePair("ctl00$ContentPlaceHolder1$btnNext.y", btn_y),

				new NameValuePair("ctl00%24ContentPlaceHolder1%24pageNo", ""),
				new NameValuePair("ctl00%24ContentPlaceHolder1%24statusDDList",
						"2"),
				new NameValuePair(
						"ctl00_ContentPlaceHolder1_categoryTreeView_ExpandState",
						"enennnnnnnnnnnnnnnnnnnnnnnnnnnn") };

		PostMethod post = new PostMethod(
				"http://www.catarc.org.cn/standard/SearchStandard.aspx");
		post.setRequestBody(data);
		client.executeMethod(post);

		String html = post.getResponseBodyAsString();

		setViewState(html);

		setEventValidation(html);

		setStandardIdsToMap(pageNo, html);

	}

	public static void setViewState(String html) throws Exception {
		Parser parser = Parser.createParser(html, "gb2312");
		AndFilter filter = new AndFilter(new TagNameFilter("input"),
				new HasAttributeFilter("id", "__VIEWSTATE"));

		NodeList nodes = parser.parse(filter);
		InputTag node = (InputTag) nodes.elementAt(0);

		viewState = node.getAttribute("value");
	}

	public static void setEventValidation(String html) throws ParserException {
		Parser parser = Parser.createParser(html, "gb2312");
		AndFilter filter = new AndFilter(new TagNameFilter("input"),
				new HasAttributeFilter("id", "__EVENTVALIDATION"));
		NodeList nodes = parser.parse(filter);
		InputTag node = (InputTag) nodes.elementAt(0);

		eventValidation = node.getAttribute("value");
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
	public static int randFloat(int max, int min) {
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
	public static void wait(int time) {
		long now = System.currentTimeMillis();
		logger.debug("......................等待" + time + "秒...........................");

		while (true) {
			long end = System.currentTimeMillis();
			
			if (end - now >= time * 1000) {
				break;
			}
		}

	}
}
