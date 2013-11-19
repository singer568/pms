package com.glodon.catchweb;

import java.io.File;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.tags.LinkTag;

public class CatchJob {

	private DBManager dbManager;

	public String catchJob(XMLObj xmlObj) {

		String document = null;
		List allLinks = null;

		try {

			// 获取网页内容
			document = ExtractPageBusiness.getContentByUrl(xmlObj.getUrl(),
					xmlObj.getCodeKind());
			// 获取页面指定内容的Link
			if ((xmlObj.getIsLandJY() != null)
					&& (xmlObj.getIsLandJY().equals("True"))) {
				document = document.replaceAll("href", "ahref").replaceAll(
						"onclick", "href");
				allLinks = ExtractPageBusiness.getLinksByConditions2(document,
						xmlObj.getLinkCondition(), xmlObj.getCodeKind());
			} else {
				allLinks = ExtractPageBusiness.getLinksByConditions(document,
						xmlObj.getLinkCondition(), xmlObj.getCodeKind());
			}

			if (allLinks != null && !allLinks.isEmpty()) {
				String articleName, articleLink;
				int beginIndex, endIndex;
				for (int i = 0; i < allLinks.size(); i++) {
					LinkTag link = (LinkTag) allLinks.get(i);
					// System.out.println(link.getLink());
					if ((xmlObj.getRemoveLeft() != null)
							&& (xmlObj.getRemoveLeft() != "")) {
						beginIndex = link.getLink().indexOf(
								xmlObj.getRemoveLeft())
								+ xmlObj.getRemoveLeft().length();
						endIndex = link.getLink().indexOf(
								xmlObj.getRemoveRight(), beginIndex);
						// System.out.println(beginIndex);
						// System.out.println(endIndex);
						articleLink = link.getLink().substring(beginIndex,
								endIndex);
						articleLink = xmlObj.getLinkPre() + articleLink;
					} else {
						articleLink = xmlObj.getLinkPre() + link.getLink();
					}
					articleLink = articleLink.replaceAll("&amp;", "&");

					if ((xmlObj.getIsLandJY() != null)
							&& (xmlObj.getIsLandJY().equals("True"))) {
						Node nextNode = link.getParent().getNextSibling();
						articleName = nextNode.getChildren().toHtml()
								.replaceAll("/r/n", "").trim();
						nextNode = nextNode.getNextSibling().getNextSibling();
						articleName = articleName
								+ ":"
								+ nextNode.getChildren().toHtml().replaceAll(
										"/r/n", "").trim();
					} else {
						articleName = link.getChildrenHTML().replaceAll("/r/n",
								"").replaceAll("&nbsp;", "");
					}

					// dbManager.insertArticleRecord(xmlObj.getChannelID(),
					// articleName.trim(), articleLink);
					System.out.println(articleLink);
					System.out.println(articleName.trim());
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "success";

	}

	public void DoCatch() {
		File directory = new File("");
		// System.out.println(directory.getPath());
		String path = "";
		try {
			path = directory.getAbsolutePath() + "/src/main/resources/websites.xml";
			if (!(new File(path).exists())) {
				JarUtil ju = new JarUtil(JarUtil.class);
				System.out.println("Jar name: " + ju.getJarName());
				System.out.println("Jar path: " + ju.getJarPath());
				path = ju.getJarPath() + "/websites.xml";
			}
			System.out.println(path);
		} catch (Exception e) {

		}
		XMLObjList xmlObjList = new XMLObjList(path);
		List list = xmlObjList.getXMLObjList();
		MySQLObj mySQLObj = xmlObjList.getMySQLObj();
		System.out.println(mySQLObj.getHost() + "; " + mySQLObj.getPort() + ";"
				+ mySQLObj.getDatabase() + ";" + mySQLObj.getUser() + ";"
				+ mySQLObj.getPassword());
		// dbManager = new DBManager(mySQLObj.getHost(), mySQLObj.getPort(),
		// mySQLObj.getDatabase(),
		// mySQLObj.getUser(), mySQLObj.getPassword());
		for (int i = 0; i < list.size(); i++) {
			XMLObj xmlObj = (XMLObj) list.get(i);
			System.out.println(xmlObj.getName() + ": " + catchJob(xmlObj));
			System.out.println("///////////////////////////////////////////");
			System.out.println("");
		}
	}

}
