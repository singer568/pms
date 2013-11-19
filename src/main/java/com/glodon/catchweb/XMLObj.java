package com.glodon.catchweb;

public class XMLObj {
	
	private String url; //要解析的主界面
	private String linkCondition; //链接过滤条件
	private String codeKind; //编码种类
	private String linkPre;
	private String name;
	private String channelID;
	private String removeLeft = "";
	private String removeRight = "";
	private String isLandJY = "False";
	
	public String getIsLandJY() {
		return isLandJY;
	}
	public void setIsLandJY(String isLandJY) {
		this.isLandJY = isLandJY;
	}
	public String getRemoveLeft() {
		return removeLeft;
	}
	public void setRemoveLeft(String removeLeft) {
		this.removeLeft = removeLeft;
	}
	public String getRemoveRight() {
		return removeRight;
	}
	public void setRemoveRight(String removeRight) {
		this.removeRight = removeRight;
	}
	public String getChannelID() {
		return channelID;
	}
	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLinkPre() {
		return linkPre;
	}
	public void setLinkPre(String linkPre) {
		this.linkPre = linkPre;
	}
	public XMLObj(){
		this.url = "";
		this.linkCondition = "";
		this.codeKind = "GB2312";
	
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLinkCondition() {
		return linkCondition;
	}
	public void setLinkCondition(String linkCondition) {
		this.linkCondition = linkCondition;
	}
	public String getCodeKind() {
		return codeKind;
	}
	public void setCodeKind(String codeKind) {
		this.codeKind = codeKind;
	}
	
	
	
	

	
}
