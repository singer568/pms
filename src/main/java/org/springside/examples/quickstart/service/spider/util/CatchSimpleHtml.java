package org.springside.examples.quickstart.service.spider.util;

import java.net.URL;
import java.util.ArrayList;
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
import org.springside.examples.quickstart.entity.Subjects;
import org.springside.examples.quickstart.entity.Url;
import org.springside.examples.quickstart.service.spider.SubjectsService;

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

	@Autowired
	public void setSubjService(SubjectsService subjService) {
		this.subjService = subjService;
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

	}

	private boolean isExistSubject(Subjects subj) {
		Date publishDate = subj.getPublishDate();
		String relativeUrl = subj.getRelativeUrl();
		String subjUrl = subj.getSubjUrl();
		String subject = subj.getSubject();

		String pubDate = DateUtil.formatDate(publishDate);

		Map<String, Object> param = new HashMap<String, Object>();

//		param.put("EQ_publishDate", pubDate);
		param.put("LIKE_relativeUrl", relativeUrl);
		param.put("LIKE_subjUrl", subjUrl);
		param.put("LIKE_subject", subject);

		Page<Subjects> subjs = subjService.querySubjectsByParam(param, "auto");
		if (null == subjs || subjs.getNumberOfElements() <= 0) {// 当前页为0
			return false;
		} else
			return true;

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
	public List<Subjects> catchPolicy(Url url) {
		TagNode html = getCleanedHtml(url.getUrl(), url.getCharset());

		if (null == html) {
			return null;
		}
		
		List<Subjects> subjs = convertLatestItems(html, url);
		
		List<Subjects> result = new ArrayList<Subjects>();
		
		for(int i =0 ; i < subjs.size(); i++){
			result.add(subjService.saveSubjects(subjs.get(i)));
		}
		
		return result;
	}

	/*
	 * private List<Subjects> convertHuanBaoBu(TagNode html, Url url, String
	 * currDate) { StringBuffer errMsg = new StringBuffer(); List<Subjects> lst
	 * = new ArrayList<Subjects>(); String subjPath = url.getSubjPath();//
	 * 选取的主题的xpath
	 * 
	 * String datePath = url.getDatePath(); String dateReplace =
	 * url.getDateReplace(); int startBegin = url.getStartBegin() == null ? 1 :
	 * url.getStartBegin(); for (int i = startBegin; i < 20 + startBegin; i++) {
	 * String datePathTmp = datePath.replaceAll(PARAMETER, i + ""); Object[]
	 * dateNode = null; try { dateNode = html.evaluateXPath(datePathTmp); }
	 * catch (XPatherException e) { errMsg.append( "抓取【" + url.getUrl() + "】第【"
	 * + i + "】发布日期xpath【" + datePath + "】时出错，异常信息为【").append(e).append( "】\n");
	 * logger.error(e); return null; } String publishDate = getText(((TagNode)
	 * dateNode[0]));
	 * 
	 * Date date = constructDate(publishDate, dateReplace);// 得到发布日期
	 * 
	 * String subjPathTmp = subjPath.replaceAll(PARAMETER, i + ""); Object[]
	 * subjNode = null; String[] subjHref = null;
	 * 
	 * String subj = null; String href = null; String relativeHref = null; try {
	 * subjNode = html.evaluateXPath(subjPathTmp);
	 * 
	 * subjHref = dealScript(subjNode);
	 * 
	 * subj = subjHref[0];
	 * 
	 * relativeHref = subjHref[1];
	 * 
	 * href = constructHref(relativeHref, url.getUrlPrefix());// 得到主题的绝对地址 }
	 * catch (Exception e) { errMsg.append( "抓取【" + url.getUrl() + "】第【" + i +
	 * "】主题xpath【" + subjPath + "】时出错，异常信息为【").append(e).append( "】\n");
	 * logger.error(e); return null; }
	 * 
	 * Subjects subjects = constructSubjects(url, subj, date, relativeHref,
	 * href);
	 * 
	 * if (isExistSubject(subjects)) {// 当前库中已经存在此主题，则直接退出，不再抓取 break; }
	 * 
	 * if (subjects != null) lst.add(subjects); if (errMsg.length() > 0) {
	 * logger.error(errMsg); } }
	 * 
	 * return lst; }
	 * 
	 * private String[] dealScript(Object[] subjNode) {
	 * 
	 * String script = ((TagNode) subjNode[0]).getText().toString(); String[]
	 * strs = script.split(";"); String subj = strs[0]; String href = strs[2];
	 * href = href.replaceAll("document.write('<a ", ""); href =
	 * href.replaceAll("class=hh14 target=\"_blank\">')", "").trim(); href =
	 * href.replaceAll("\"", ""); href = href.split("=")[1];
	 * 
	 * String[] tmps = subj.split("="); subj = tmps[1].trim(); subj =
	 * subj.replaceFirst("'", ""); subj = subj.substring(0,
	 * subj.lastIndexOf("'"));
	 * 
	 * String[] result = new String[] { subj, href }; return result; }
	 */
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
		subject.setCatchTime(new Date());
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

	private List<Subjects> convertLatestItems(TagNode html, Url url) {
		StringBuffer errMsg = new StringBuffer();
		List<Subjects> lst = new ArrayList<Subjects>();
		String subjPath = url.getSubjPath();// 选取的主题的xpath

		String linkPath = url.getLinkPath();// 主题内部连接的xpath
		String datePath = url.getDatePath();
		String dateReplace = url.getDateReplace();
		Date date = null;
		String subj = null;
		String relativeHref = null;
		String href = null;
		int startBegin = url.getStartBegin() == null ? 1 : url.getStartBegin();
		for (int i = startBegin; i < 20 + startBegin; i++) {

			if (datePath != null) {// 当前待抓取网址无日期字段
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
					return null;
				}
				if (dateNode == null || dateNode.length == 0) {
					return lst;
				}

				String publishDate = getText(((TagNode) dateNode[0]));

				date = constructDate(publishDate, dateReplace);// 得到发布日期
			}

			if (subjPath != null) {
				String subjPathTmp = subjPath.replaceAll(PARAMETER, i + "");
				Object[] subjNode = null;
				try {
					subjNode = html.evaluateXPath(subjPathTmp);
					subj = constructSubjectItem(((TagNode) subjNode[0])
							.getText().toString(), url.getSubjReplace());// 得到主题
					subj = replaceCData(subj);
				} catch (Exception e) {
					errMsg.append(
							"抓取【" + url.getUrl() + "】第【" + i + "】主题xpath【"
									+ subjPath + "】时出错，异常信息为【").append(e)
							.append("】\n");
					logger.error(e);
					return null;
				}

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
					relativeHref = replaceCData(relativeHref);
					href = constructHref(relativeHref, url.getUrlPrefix());// 得到主题的绝对地址
				} catch (Exception e) {
					errMsg.append(
							"抓取【" + url.getUrl() + "】第【" + i + "】链接xpath【"
									+ linkPath + "】时出错，异常信息为【").append(e)
							.append("】\n");
					logger.error(e);
					return null;
				}

			}

			Subjects subjects = constructSubjects(url, subj, date,
					relativeHref, href);

			if (isExistSubject(subjects)) {// 当前库中已经存在此主题，则直接退出，不再抓取
				break;
			}

			if (subjects != null)
				lst.add(subjects);
			if (errMsg.length() > 0) {
				logger.error(errMsg);
			}
		}

		return lst;
	}

	private String getText(TagNode node) {
		List lst = node.getAllChildren();
		for (int i = 0; i < lst.size(); i++) {
			Object item = lst.get(i);
			if (item instanceof ContentNode) {
				return ((ContentNode) item).getContent();
			}

		}
		return null;
	}

	private Subjects constructSubjects(Url url, String subj, Date date,
			String relativeHref, String href) {
		Subjects subject = constructSubjects(url, subj, relativeHref, href);
		subject.setRelativeUrl(relativeHref);
		subject.setPublishDate(date);
		return subject;
	}

	private Date constructDate(String publishDate, String dateReplace) {
		if (StringUtil.isNull(dateReplace)) {
			return DateUtil.formatDate(publishDate);
		}
		String[] strs = dateReplace.split("#");
		for (int i = 0; i < strs.length; i++) {
			String[] arr = strs[i].split("=");

			if (arr.length == 1) {
				if (specialChar.contains(arr[0])) {
					publishDate = publishDate.replaceAll("\\" + arr[0], "");
				} else
					publishDate = publishDate.replaceAll(arr[0], "");
			} else {
				if (specialChar.contains(arr[0])) {
					publishDate = publishDate.replaceAll("\\" + arr[0], arr[1]);
				} else
					publishDate = publishDate.replaceAll(arr[0], arr[1]);
			}

		}
		return DateUtil.formatDate(publishDate.trim());
	}

	private TagNode getCleanedHtml(String url, String charset) {
		URLAvailability avalible = new URLAvailability();
		URL u = avalible.isConnect(url);
		if (u == null) {
			return null;
		}

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

			node = cleaner.clean(new java.net.URL(url), charset);

		} catch (HttpException e) {
			System.out.println("Please check your provided http address");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
