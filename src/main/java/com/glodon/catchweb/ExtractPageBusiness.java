package com.glodon.catchweb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class ExtractPageBusiness {

	private static final int CatchNum = 10;
	private static final String MaxTime = "60000";//60s超时等待
	
	private static final String loginUrl = "http://www.catarc.org.cn/standard/Default.aspx";//登录连接
	
	private static final String searchResultUrl = "http://www.catarc.org.cn/standard/SearchStandard.aspx";//带上一页、下一页的搜索页面
	
	private static final String detialUrl = "http://www.catarc.org.cn/standard/StandardDetail.aspx";//具体某一条的详细信息页面，参数standardid
	
	

	// 抓取页面内容
	public static String getContentByUrl(String url, String codeKind) {
		StringBuffer document = null;
		URL targetUrl;
		try {
			targetUrl = new URL(url);
			System.setProperty("sun.net.client.defaultConnectTimeout", MaxTime);
			System.setProperty("sun.net.client.defaultReadTimeout", MaxTime);
			HttpURLConnection con = (HttpURLConnection) targetUrl
					.openConnection();
			con.setFollowRedirects(true);
			con.setInstanceFollowRedirects(false);
			con.connect();

			BufferedReader br = new BufferedReader(new InputStreamReader(con
					.getInputStream(), codeKind));
			String s = "";
			document = new StringBuffer();
			while ((s = br.readLine()) != null) {
				document.append(s + "/r/n");
			}
			s = null;
			br.close();
			return document.toString();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	 public static String login() throws Exception{  
	        //登录  
	        URL url = new URL(loginUrl);  
	        String param = "__VIEWSTATE=%2FwEPDwUJNzE2NjczNjA4ZGQCaV1Dt0DOUg5r7O3ZD%2BsGL1Hq6Q%3D%3D&__EVENTVALIDATION=%2FwEWBQK%2F2uyqBgKl1bKzCQK1qbSRCwLZpaCZAwKTuvSuCTYE9T8nbPNCPEY2eqmCy5I3BwtG&txtUserName=asri&txtPassword=asri&loginBtn=%B5%C7%C2%BD";  
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();  
	        conn.setDoInput(true);  
	        conn.setDoOutput(true);  
	        conn.setRequestMethod("POST");  
	        OutputStream out = conn.getOutputStream();  
	        out.write(param.getBytes());  
	        out.flush();  
	        out.close();  
	        String sessionId = "";  
	        String cookieVal = "";  
	        String key = null;  
	        //取cookie  
	        for(int i = 1; (key = conn.getHeaderFieldKey(i)) != null; i++){  
	            if(key.equalsIgnoreCase("set-cookie")){  
	                cookieVal = conn.getHeaderField(i);  
	                cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));  
	                sessionId = sessionId + cookieVal + ";";  
	            }  
	        }  
	        return sessionId;  
	    }  
	
	// 获取页面指定内容的Link
	public static List getLinksByConditions(String result, String coditions,
			String codeKind) {
		List links = null;
		Parser parser;
		NodeList nodelist;
		// 页面编码配置 To do by shengf
		parser = Parser.createParser(result, codeKind);
		NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
		try {
			links = new ArrayList();
			nodelist = parser.parse(linkFilter);
			Node[] nodes = nodelist.toNodeArray();
			int count = 1;
			for (int i = 0; i < nodes.length; i++) {
				Node node = nodes[i];
				if (node instanceof LinkTag) {
					LinkTag link = (LinkTag) node;
					if (link.toHtml().indexOf(coditions) != -1) {
						links.add(link);
						count++;
						if (count > CatchNum) {
							return links;
						}
					}
				}
			}
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return links;
	}

	// 土地交易单独处理
	public static List getLinksByConditions2(String result, String coditions,
			String codeKind) {
		List links = null;
		Parser parser;
		NodeList nodelist;
		parser = Parser.createParser(result, codeKind);
		NodeFilter linkFilter = new NodeClassFilter(LinkTag.class);
		try {
			links = new ArrayList();
			nodelist = parser.parse(linkFilter);
			Node[] nodes = nodelist.toNodeArray();
			int count = 1;
			for (int i = 0; i < nodes.length; i++) {
				Node node = nodes[i];
				if (node instanceof LinkTag) {
					LinkTag link = (LinkTag) node;
					if ((link.toHtml().indexOf(coditions) != -1)
							&& (link.getChildrenHTML().indexOf("查看") == -1)) {
						// System.out.println(link.toHtml());
						// System.out.println(link.getLink());
						// System.out.println("test:" + link.getChildrenHTML());
						// Node nextNode = link.getParent().getNextSibling();
						// System.out.println(nextNode.getChildren().toHtml().replaceAll("/r/n","").trim());
						// nextNode =
						// nextNode.getNextSibling().getNextSibling();
						// System.out.println(nextNode.getChildren().toHtml().replaceAll("/r/n","").trim());
						links.add(link);
						count++;
						if (count > CatchNum) {
							return links;

						}
					}
				}
			}
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return links;
	}
}
