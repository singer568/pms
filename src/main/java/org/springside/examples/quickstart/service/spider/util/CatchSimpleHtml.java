package org.springside.examples.quickstart.service.spider.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.ContentNode;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.quickstart.entity.KeyWords;
import org.springside.examples.quickstart.entity.Subjects;
import org.springside.examples.quickstart.entity.Url;
import org.springside.examples.quickstart.service.spider.SubjectsService;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@Component
@Transactional
public class CatchSimpleHtml implements CatchService {

	private static Logger logger = Logger.getLogger(CatchSimpleHtml.class
			.getName());

	public static final String PARAMETER = "parameter";

	private List<String> specialChar = null;

	private SubjectsService subjService;

	private String cdata_s = "<!\\[CDATA\\[";
	private String cdata_e = "\\]\\]>";

	private String isInit = "1";

	@Autowired
	public void setSubjService(SubjectsService subjService) {
		this.subjService = subjService;
	}

	public Subjects updateSubject(Subjects subj) throws Exception {
		return subjService.saveSubjects(subj);
	}

	public List<Subjects> updateSubjects(List<Subjects> lst) throws Exception {
		List<Subjects> result = new ArrayList<Subjects>();
		Subjects tmp;
		for (int i = 0; i < lst.size(); i++) {
			tmp = lst.get(i);
			result.add(subjService.saveSubjects(tmp));
		}
		return result;
	}

	public CatchSimpleHtml() {
		specialChar = new ArrayList<String>();
		specialChar.add("$");
		specialChar.add("(");
		specialChar.add(")");
		specialChar.add("*");
		specialChar.add("+");
		specialChar.add(".");
		specialChar.add("[");
		specialChar.add("]");
		specialChar.add("?");
		specialChar.add("\\");
		specialChar.add("^");
		specialChar.add("{");
		specialChar.add("}");
		specialChar.add("|");

		isInit = SystemUtil.getInstance().getSystemConfig("isInit");
	}

	private boolean isExistSubject(Subjects subj) {
		String relativeUrl = subj.getRelativeUrl();
		String subjUrl = subj.getSubjUrl();
		String subject = subj.getSubject();

		Map<String, Object> param = new HashMap<String, Object>();

		param.put("LIKE_relativeUrl", relativeUrl);
		param.put("LIKE_subjUrl", subjUrl);
		param.put("LIKE_subject", subject);

		List<Subjects> subjs = subjService.querySubjectsByParam(param, "auto");
		if (null == subjs || subjs.size() <= 0) {// 当前页为0
			return false;
		} else
			return true;

	}

	public List<Subjects> catchPolicy(Url url) throws Exception {
		return catchPolicy(url, null);
	}

	/**
	 * 取某天某网址的主题 一个网址产出一批主题
	 * 
	 * @param url
	 *            给定网址
	 * @param catchDate
	 *            抓取日期
	 * @return
	 */
	public List<Subjects> catchPolicy(Url url, List<KeyWords> keyWords)
			throws Exception {

		List<Subjects> result = new ArrayList<Subjects>();

		int defaultTimes = 1;
		String catchNextPage = url.getCatchNextPage();// 每次进入时都将下一页的原始标记保存起来，后面会改动此值
		String urlStr = url.getUrl();// 原始的url临时保存起来
		while (true) {
			result.addAll(catchOnce(url, keyWords, defaultTimes));
			if (!"1".equals(url.getCatchNextPage())) {// 不需要取下一页
				url.setCatchNextPage(catchNextPage);
				url.setUrl(urlStr);
				break;
			}
			if ("1".equals(isInit) && defaultTimes == 2) {
				url.setCatchNextPage(catchNextPage);
				url.setUrl(urlStr);
				break;
			}
			++defaultTimes;
		}
		return result;
	}

	private List<Subjects> catchOnce(Url url, List<KeyWords> keyWords, int times)
			throws Exception {
		String catchType = url.getCatchType();
		TagNode html = null;
		if ("NORMAL".equals(catchType)) {
			html = getCleanedHtml(url.getUrl(), url.getCharset());
		}
		if ("SCRIPT".equals(catchType)) {
			html = getScriptHtml(url.getUrl());
		}

		if (null == html) {
			return null;
		}

		List<Subjects> subjs = convertLatestItems(html, url);

		if ("1".equals(url.getCatchNextPage())) {// 需要抓取下一页
			String nextPage = url.getNextPageXpath();
			Object[] nodes = (Object[]) html.evaluateXPath(nextPage);
			if (null == nodes || nodes.length <= 0) {
				logger.error("找不到下一页对应的连接{" + url.toString() + "},当前是第{"
						+ times + "}页");
				url.setCatchNextPage("0");
				return subjs;
			}

			String nextPageUrl = ((TagNode) nodes[0])
					.getAttributeByName("href");
			String urlStr = constructHref(nextPageUrl, url.getUrlPrefix());
			url.setUrl(urlStr);
		}
//		Subjects tmp = null;
//		for (int i = 0; i < subjs.size(); i++) {
//
//			tmp = subjs.get(i);
//			if (keyWords != null) {
//				setSubjectsKeyFlag(tmp, keyWords);// 设置keyFlag
//			}
//		}
		return subjs;
	}

	private void setSubjectsKeyFlag(Subjects tmp, List<KeyWords> keyWords) {
		for (int i = 0; i < keyWords.size(); i++) {
			if (keyWords.get(i).getKeyWords() == null) {
				continue;
			}
			if (tmp.getSubject().contains(keyWords.get(i).getKeyWords())) {
				tmp.setKeyFlag("1");
				return;
			}
		}
	}

	private String replaceCData(String str) {
		str = str.replaceAll(cdata_e, "");
		str = str.replaceAll(cdata_s, "");
		return str;
	}

	private Subjects constructSubjects(Url url, String subj,
			String relativeHref, String href) {
		if (null == subj || href == null) {
			return null;
		}

		Subjects subject = new Subjects();
		subject.setUrl(url);
		subject.setSubject(subj);
		subject.setSubjUrl(href);
		subject.setFromUrl(url.getUrl());
		subject.setCatchTime(getCurrentDate());
		subject.setRelativeUrl(relativeHref);

		return subject;
	}

	private String constructSubjectItem(String subject, String replace) {
		if (StringUtil.isNull(replace)) {
			return subject;
		}
		String[] strs = replace.split("#");
		for (int i = 0; i < strs.length; i++) {
			String[] arr = strs[i].split("=");
			if (arr.length == 1) { // 替换成空串的情况
				if (specialChar.contains(arr[0])) {
					subject = subject.replaceAll("\\" + arr[0], "");
				} else
					subject = subject.replaceAll(arr[0], "");
			} else {
				if (specialChar.contains(arr[0])) {
					subject = subject.replaceAll("\\" + arr[0], arr[1]);
				} else
					subject = subject.replaceAll(arr[0], arr[1]);
			}

		}
		return subject;
	}

	private String constructHref(String href, String prefix) {
		if (href.startsWith("http:")) {
			return href;
		}
		if (href.startsWith("/")) {
			if (prefix.endsWith("/")) {
				return prefix + href.replaceFirst("/", "");
			} else {
				return prefix + href;
			}
		} else {
			if (prefix.endsWith("/")) {
				return prefix + href;
			} else {
				return prefix + href.replaceFirst("/", "");
			}
		}
	}

	private List<Subjects> convertLatestItems(TagNode html, Url url)
			throws Exception {
		StringBuffer errMsg = new StringBuffer();
		List<Subjects> lst = new ArrayList<Subjects>();
		String subjPath = url.getSubjPath();// 选取的主题的xpath

		String linkPath = url.getLinkPath();// 主题内部连接的xpath
		String datePath = url.getDatePath();
		Date date = null;
		String publishDate = null;
		String subj = null;
		String relativeHref = null;
		String href = null;
		int startBegin = url.getStartBegin() == null ? 1 : url.getStartBegin();

		int i = startBegin;
		while (true) {
//			System.out.println(i);
			if (i > 50) {
				break;
			}
			
			if (subjPath != null) {// 调整为先抓主题，主题每个网站都会存在，日期不一定
				
				if (!subjPath.contains(PARAMETER)){
					throw new Exception("不包含parameter参数");
				}
				
				
				String subjPathTmp = subjPath.replaceAll(PARAMETER, i + "");
				Object[] subjNode = null;
				try {
					subjNode = html.evaluateXPath(subjPathTmp);

					if ((subjNode == null || subjNode.length == 0) && i < 36) {
						++i;
						continue;
					}
					if (subjNode == null || subjNode.length == 0) {// /当i增长到一定大小时，无法取得日期值，则直接退出
						return lst;
					}
					subj = constructSubjectItem(((TagNode) subjNode[0])
							.getText().toString(), url.getSubjReplace());// 得到主题

					subj = replaceSpecialChar(subj);

					subj = replaceCData(subj.trim());
				} catch (Exception e) {
					errMsg.append(
							"抓取【" + url.getUrl() + "】第【" + i + "】主题xpath【"
									+ subjPath + "】时出错，异常信息为【").append(e)
							.append("】\n");
					logger.error(e);
					throw e;
				}
			}
			if (datePath != null && !datePath.trim().equals("")) {
				String datePathTmp = datePath.replaceAll(PARAMETER, i + "");
				Object[] dateNode = null;
				try {
					dateNode = html.evaluateXPath(datePathTmp);
				} catch (XPatherException e) {
					errMsg.append(
							"抓取【" + url.getUrl() + "】第【" + i + "】发布日期xpath【"
									+ datePath + "】时出错，异常信息为【").append(e)
							.append("】\n");
					logger.error(e);
					throw e;
				}

				publishDate = getText(((TagNode) dateNode[0]));

				publishDate = publishDate.trim();

				publishDate = publishDate.replace(" ", "");
				publishDate = publishDate.replace("&nbsp;", "");

				date = DateUtil.getCurrentDate();// 所有日期默认当前日期
			} else { // 当前主题没有发布日期，则设置当前抓取时间为默认时间
				date = DateUtil.getCurrentDate();
			}

			if (linkPath != null) {

				Object[] linkNode = null;
				String linkPathTmp = linkPath.replaceAll(PARAMETER, i + "");
				try {
					linkNode = html.evaluateXPath(linkPathTmp);
					relativeHref = ((TagNode) linkNode[0])
							.getAttributeByName("href") == null ? ((TagNode) linkNode[0])
							.getText().toString()
							: ((TagNode) linkNode[0])
									.getAttributeByName("href");
					relativeHref = replaceSpecialChar(relativeHref);
					relativeHref = replaceCData(relativeHref.trim());
					href = constructHref(relativeHref, url.getUrlPrefix());// 得到主题的绝对地址
				} catch (Exception e) {
					errMsg.append(
							"抓取【" + url.getUrl() + "】第【" + i + "】链接xpath【"
									+ linkPath + "】时出错，异常信息为【").append(e)
							.append("】\n");
					logger.error(e);
					throw e;
				}

			}

			Subjects subjects = constructSubjects(url, subj, publishDate, date,
					relativeHref, href);
			
			subjects.setCode(url.getCode());
			subjects.setName(url.getName());
			subjects.setArea(url.getArea());
			subjects.setProvince(url.getProvince());
			subjects.setDepartment(url.getDepartment());
			subjects.setModule(url.getModule());
			subjects.setSubmodule(url.getSubmodule());
			subjects.setIsCapital(url.getIsCapital());
			subjects.setFilter(url.getFilter());
			subjects.setLevel(url.getLevel());
			

			if (isExistSubject(subjects)) {// 当前库中已经存在此主题，则直接退出，不再抓取
				url.setCatchNextPage("0");
				break;
			}

			if (subjects != null) {
				if (lst.size() > 0) {
					boolean isContain = false;
					for (int t = 0; t < lst.size(); t++) {
						Subjects tmp = lst.get(t);
						if (tmp.getSubjUrl() != null && tmp.getSubjUrl().equals(subjects.getSubjUrl())) {
							isContain = true;
							break;
						}
					}
					if (!isContain) {
						lst.add(subjects);
					}
				} else {
					lst.add(subjects);
				}
			} 
			if (errMsg.length() > 0) {
				logger.error(errMsg);
			}
			++i;// 序号加1
		}
		return lst;
	}

	private String getText(TagNode node) {
		List lst = node.getAllChildren();
		for (int i = 0; i < lst.size(); i++) {
			Object item = lst.get(i);
			if (item instanceof ContentNode) {
				String tmp = ((ContentNode) item).getContent();
				tmp = replaceSpecialChar(tmp);
				if (!"".equals(tmp)) {
					return tmp;
				}
			}
		}
		return null;
	}

	private String replaceSpecialChar(String str) {
		str = str.replaceAll("\r", "");
		str = str.replaceAll("\n", "");
		return str;
	}

	private Subjects constructSubjects(Url url, String subj,
			String publishDate, Date date, String relativeHref, String href) {
		if (null == subj || href == null) {
			return null;
		}

		Subjects subject = new Subjects();
		subject.setLevel(url.getLevel());
		subject.setUrl(url);
		subject.setSubject(subj);
		subject.setSubjUrl(href);
		subject.setFromUrl(url.getUrl());
		subject.setCatchTime(getCurrentDate());

		subject.setRelativeUrl(relativeHref);
		subject.setPublishDate(publishDate);
		return subject;
	}

	private String getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(cal.getTime());
	}

	private TagNode getScriptHtml(String url) throws Exception {
		WebClient wc = new WebClient();

		wc.getOptions().setJavaScriptEnabled(true); // 启用JS解释器，默认为true
		wc.getOptions().setCssEnabled(false); // 禁用css支持
		wc.getOptions().setThrowExceptionOnScriptError(false); // js运行错误时，是否抛出异常
		wc.getOptions().setTimeout(10000); // 设置连接超时时间 ，这里是10S。如果为0，则无限期等待

		HtmlPage page = wc.getPage(url);

		String pageXml = page.asXml(); // 以xml的形式获取响应文本

		HtmlCleaner cleaner = new HtmlCleaner();
		CleanerProperties props = cleaner.getProperties();
		props.setAllowHtmlInsideAttributes(true);
		props.setAllowMultiWordAttributes(true);
		props.setRecognizeUnicodeChars(true);
		props.setOmitComments(true);

		TagNode node = cleaner.clean(pageXml);
		Object[] nsscript = node.evaluateXPath("//script");
		for (Object tt : nsscript) {
			((TagNode) tt).removeFromTree();
		}
		return node;
	}

	private TagNode getCleanedHtml(String url, String charset) throws Exception {

		TagNode node = null;
		try {

			CleanerProperties props = new CleanerProperties();
			props.setUseCdataForScriptAndStyle(false);
			props.setRecognizeUnicodeChars(true);
			props.setUseEmptyElementTags(true);
			props.setAdvancedXmlEscape(true);
			props.setTranslateSpecialEntities(true);
			props.setBooleanAttributeValues("empty");
			props.setIgnoreQuestAndExclam(false);
			HtmlCleaner cleaner = new HtmlCleaner(props);

			if (charset == null || charset.trim().equals("")) {
				throw new Exception("编码为空");
			}
			
			node = cleaner.clean(new java.net.URL(url), charset);

		} catch (HttpException e) {
			System.out.println("Please check your provided http address");
			e.printStackTrace();
			throw new Exception(e.getMessage() + "{" + url + "}", e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage() + "{" + url + "}", e.getCause());
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

	public static void main(String[] args) {
		String tmp = "20140506";
		if (tmp.length() == 8) {// 8位：20140101，转化为10位
			String y = tmp.substring(0, 4);
			String m = tmp.substring(4, 6);
			String d = tmp.substring(6, 8);
			tmp = y + "-" + m + "-" + d;
		}

		System.out.println(tmp);
		Date d = DateUtil.formatDate(tmp);
		System.out.println(d);

	}

	private static void changeValue(int[] a) {
		a[0] = 10;
	}
}
