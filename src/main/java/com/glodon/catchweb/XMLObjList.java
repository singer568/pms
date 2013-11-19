package com.glodon.catchweb;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLObjList {
	
	private Element root = null;
	
	public XMLObjList(String path){
		try{
			File file = new File(path);
			SAXReader reader = new SAXReader(); // 使用SAX方式解析XML
			Document doc = reader.read(file); // 将XML文件解析成文档对象
			root = doc.getRootElement(); // 取得根节点
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

	public List getXMLObjList(){
		List xmlObjList = new ArrayList();		
		try {
			List websites = (List<Element>)root.elements();
			Element website;
			for(int i = 0; i < websites.size(); i++){
				XMLObj xmlObj = new XMLObj();
				//System.out.println(websites.get(i).toString());
				website = (Element)websites.get(i);
				xmlObj.setName(website.attributeValue("Name"));
				xmlObj.setUrl(website.attributeValue("URL").replace(">>", "&"));
				xmlObj.setLinkPre(website.attributeValue("LinkPre").replace(">>", "&"));
				xmlObj.setLinkCondition(website.attributeValue("LinkCondition"));
				xmlObj.setCodeKind(website.attributeValue("CodeKind"));
				xmlObj.setChannelID(website.attributeValue("ChannelID"));
				xmlObj.setRemoveLeft(website.attributeValue("RemoveLeft"));
				xmlObj.setRemoveRight(website.attributeValue("RemoveRight"));
				xmlObj.setIsLandJY(website.attributeValue("IsLandJY"));
				xmlObjList.add(xmlObj);
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
				
		return xmlObjList;
	}
	
	
	public MySQLObj getMySQLObj(){
		MySQLObj mySQLObj = new MySQLObj();
		mySQLObj.setHost(root.attributeValue("Host"));
		mySQLObj.setPort(root.attributeValue("Port"));
		mySQLObj.setDatabase(root.attributeValue("Database"));
		mySQLObj.setUser(root.attributeValue("User"));
		mySQLObj.setPassword(root.attributeValue("Password"));
		
		return mySQLObj;
	}

}
