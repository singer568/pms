package com.glodon.catchweb;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.util.EncodingUtil;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.util.DefaultParserFeedback;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * mht文件解析类
 * 
 * @author dl
 */
public class Html2MHTCompiler {
	private URL strWeb = null;
	/** 网页地址 */
	private String strText = null;
	/** 网页文本内容 */
	private String strFileName = null;
	/** 本地文件名 */
	private String strEncoding = null;
	/** 网页编码 */
	// mht格式附加信息
	private String from = "dongle2001@126.com";
	private String to;
	private String subject = "mht compile";
	private String cc;
	private String bcc;
	private String smtp = "localhost";

	public static void main(String[] args) {
		String strUrl = "http://www.mtime.com/my/tropicofcancer/blog/843555/";
		String strEncoding = "utf-8";
		String strText = null;
			//TODO 
			//JQuery.getHtmlText(strUrl, strEncoding, null);
		if (strText == null)
			return;
		Html2MHTCompiler h2t = new Html2MHTCompiler(strText, strUrl,
				strEncoding, "test.mht");
		h2t.compile();
		// Html2MHTCompiler.mht2html("test.mht", "a.html");
	}

	/**
	 *<br>
	 * 方法说明：初始化 <br>
	 * 输入参数：strText 网页文本内容; strUrl 网页地址; strEncoding 网页编码; strFileName 本地文件名 <br>
	 * 返回类型：
	 */
	public Html2MHTCompiler(String strText, String strUrl, String strEncoding,
			String strFileName) {
		// TODO Auto-generated constructor stub
		try {
			strWeb = new URL(strUrl);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		this.strText = strText;
		this.strEncoding = strEncoding;
		this.strFileName = strFileName;
	}

	/**
	 *<br>
	 * 方法说明：执行下载操作 <br>
	 * 输入参数： <br>
	 * 返回类型：
	 */
	public boolean compile() {
		if (strWeb == null || strText == null || strFileName == null
				|| strEncoding == null)
			return false;
		HashMap urlMap = new HashMap();
		NodeList nodes = new NodeList();
		try {
			Parser parser = createParser(strText);
			parser.setEncoding(strEncoding);
			nodes = parser.parse(null);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		extractAllScriptNodes(nodes);
		ArrayList urlScriptList = extractAllScriptNodes(nodes, urlMap);
		ArrayList urlImageList = extractAllImageNodes(nodes, urlMap);
		for (Iterator iter = urlMap.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String) entry.getKey();
			String val = (String) entry.getValue();
			strText = null;
			//TODO 
			//JHtmlClear.replace(strText, val, key);
		}
		try {
			createMhtArchive(strText, urlScriptList, urlImageList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 *<br>
	 * 方法说明：建立HTML parser <br>
	 * 输入参数：inputHTML 网页文本内容 <br>
	 * 返回类型：HTML parser
	 */
	private Parser createParser(String inputHTML) {
		// TODO Auto-generated method stub
		Lexer mLexer = new Lexer(new Page(inputHTML));
		return new Parser(mLexer, new DefaultParserFeedback(
				DefaultParserFeedback.QUIET));
	}

	/**
	 *<br>
	 * 方法说明：抽取基础URL地址 <br>
	 * 输入参数：nodes 网页标签集合 <br>
	 * 返回类型：
	 */
	private void extractAllScriptNodes(NodeList nodes) {
		NodeList filtered = nodes.extractAllNodesThatMatch(new TagNameFilter(
				"BASE"), true);
		if (filtered != null && filtered.size() > 0) {
			Tag tag = (Tag) filtered.elementAt(0);
			String href = tag.getAttribute("href");
			if (href != null && href.length() > 0) {
				try {
					strWeb = new URL(href);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 *<br>
	 * 方法说明：抽取网页包含的css,js链接 <br>
	 * 输入参数：nodes 网页标签集合; urlMap 已存在的url集合 <br>
	 * 返回类型：css,js链接的集合
	 */
	private ArrayList extractAllScriptNodes(NodeList nodes, HashMap urlMap) {
		ArrayList urlList = new ArrayList();
		NodeList filtered = nodes.extractAllNodesThatMatch(new TagNameFilter(
				"script"), true);
		for (int i = 0; i < filtered.size(); i++) {
			Tag tag = (Tag) filtered.elementAt(i);
			String src = tag.getAttribute("src");
			// Handle external css file's url
			if (src != null && src.length() > 0) {
				String innerURL = src;
				String absoluteURL = makeAbsoluteURL(strWeb, innerURL);
				if (absoluteURL != null && !urlMap.containsKey(absoluteURL)) {
					urlMap.put(absoluteURL, innerURL);
					ArrayList urlInfo = new ArrayList();
					urlInfo.add(innerURL);
					urlInfo.add(absoluteURL);
					urlList.add(urlInfo);
				}
				tag.setAttribute("src", absoluteURL);
			}
		}
		filtered = nodes.extractAllNodesThatMatch(new TagNameFilter("link"),
				true);
		for (int i = 0; i < filtered.size(); i++) {
			Tag tag = (Tag) filtered.elementAt(i);
			String type = (tag.getAttribute("type"));
			String rel = (tag.getAttribute("rel"));
			String href = tag.getAttribute("href");
			boolean isCssFile = false;
			if (rel != null) {
				isCssFile = rel.indexOf("stylesheet") != -1;
			} else if (type != null) {
				isCssFile |= type.indexOf("text/css") != -1;
			}
			// Handle external css file's url
			if (isCssFile && href != null && href.length() > 0) {
				String innerURL = href;
				String absoluteURL = makeAbsoluteURL(strWeb, innerURL);
				if (absoluteURL != null && !urlMap.containsKey(absoluteURL)) {
					urlMap.put(absoluteURL, innerURL);
					ArrayList urlInfo = new ArrayList();
					urlInfo.add(innerURL);
					urlInfo.add(absoluteURL);
					urlList.add(urlInfo);
				}
				tag.setAttribute("href", absoluteURL);
			}
		}
		return urlList;
	}

	/**
	 *<br>
	 * 方法说明：抽取网页包含的图像链接 <br>
	 * 输入参数：nodes 网页标签集合; urlMap 已存在的url集合 <br>
	 * 返回类型：图像链接集合
	 */
	private ArrayList extractAllImageNodes(NodeList nodes, HashMap urlMap) {
		ArrayList urlList = new ArrayList();
		NodeList filtered = nodes.extractAllNodesThatMatch(new TagNameFilter(
				"IMG"), true);
		for (int i = 0; i < filtered.size(); i++) {
			Tag tag = (Tag) filtered.elementAt(i);
			String src = tag.getAttribute("src");
			// Handle external css file's url
			if (src != null && src.length() > 0) {
				String innerURL = src;
				String absoluteURL = makeAbsoluteURL(strWeb, innerURL);
				if (absoluteURL != null && !urlMap.containsKey(absoluteURL)) {
					urlMap.put(absoluteURL, innerURL);
					ArrayList urlInfo = new ArrayList();
					urlInfo.add(innerURL);
					urlInfo.add(absoluteURL);
					urlList.add(urlInfo);
				}
				tag.setAttribute("src", absoluteURL);
			}
		}
		return urlList;
	}

	/**
	 *<br>
	 * 方法说明：相对路径转绝对路径 <br>
	 * 输入参数：strWeb 网页地址; innerURL 相对路径链接 <br>
	 * 返回类型：绝对路径链接
	 */
	public static String makeAbsoluteURL(URL strWeb, String innerURL) {
		// TODO Auto-generated method stub
		// 去除后缀
		int pos = innerURL.indexOf("?");
		if (pos != -1) {
			innerURL = innerURL.substring(0, pos);
		}
		if (innerURL != null && innerURL.toLowerCase().indexOf("http") == 0) {
			System.out.println(innerURL);
			return innerURL;
		}
		URL linkUri = null;
		try {
			linkUri = new URL(strWeb, innerURL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		String absURL = linkUri.toString();
		//TODO 
//		absURL = JHtmlClear.replace(absURL, "../", "");
//		absURL = JHtmlClear.replace(absURL, "./", "");
		System.out.println(absURL);
		return absURL;
	}

	/**
	 *<br>
	 * 方法说明：创建mht文件 <br>
	 * 输入参数：content 网页文本内容; urlScriptList 脚本链接集合; urlImageList 图片链接集合 <br>
	 * 返回类型：
	 */
	private void createMhtArchive(String content, ArrayList urlScriptList,
			ArrayList urlImageList) throws Exception {
		// Instantiate a Multipart object
		MimeMultipart mp = new MimeMultipart("related");
		Properties props = new Properties();
		props.put("mail.smtp.host", smtp);
		Session session = Session.getDefaultInstance(props, null);
		MimeMessage msg = new MimeMessage(session);
		// set mailer
		msg.setHeader("X-Mailer", "Code Manager .SWT");
		// set from
		if (from != null) {
			msg.setFrom(new InternetAddress(from));
		}
		// set subject
		if (subject != null) {
			msg.setSubject(subject);
		}
		//TODO 
		// to
//		if (to != null) {
//			InternetAddress[] toAddresses = getInetAddresses(to);
//			msg.setRecipients(Message.RecipientType.TO, toAddresses);
//		}
		// cc
//		if (cc != null) {
//			InternetAddress[] ccAddresses = getInetAddresses(cc);
//			msg.setRecipients(Message.RecipientType.CC, ccAddresses);
//		}
		// bcc
//		if (bcc != null) {
//			InternetAddress[] bccAddresses = getInetAddresses(bcc);
//			msg.setRecipients(Message.RecipientType.BCC, bccAddresses);
//		}
	}

	private String getSource(String url) {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		
		method.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=GBK");
		
		//TODO 
//		method.setQueryString(EncodingUtil.formUrlEncode(params, "GBK"));
		try {
			client.executeMethod(method);

		} catch (HttpException e) {
		} catch (IOException e) {
		}
		InputStream in = null;
		try {
			in = method.getResponseBodyAsStream();
		} catch (IOException e1) {
		}
		in = new BufferedInputStream(in);
		Reader r = new InputStreamReader(in);
		int c;
		StringBuffer buffer = new StringBuffer();
		try {
			while ((c = r.read()) != -1)
				buffer.append((char) c);
		} catch (IOException e) {
		}
		try {
			in.close();
		} catch (IOException e) {
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
