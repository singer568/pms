package org.springside.examples.quickstart.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

//JPA标识
@Entity
@Table(name = "pms_subjects")
public class Subjects extends IdEntity {

	private Url url;

	/**
	 * 抓取来源网址
	 */
	private String fromUrl;

	/**
	 * 截取出来的主题的相对地址
	 */
	private String relativeUrl;

	/**
	 * 政策主题
	 */
	private String subject;

	/**
	 * 发布日期
	 */
	private Date publishDate;

	/**
	 * 发布日期时间
	 */
	private Date publishDateTime;

	/**
	 * 主题详细内容的url
	 */
	private String subjUrl;

	/**
	 * 详细内容
	 */
	private String content;

	/**
	 * 抓取时间戳
	 */
	private Date catchTime;
	
	
	/**
	 * 删除标志
	 */
	private int dr;
	
	private Level level;
	
	private Group group;
	
	
	@ManyToOne
	@JoinColumn(name = "level_id")
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}
	@ManyToOne
	@JoinColumn(name = "group_id")
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public int getDr() {
		return dr;
	}

	public void setDr(int dr) {
		this.dr = dr;
	}

	@ManyToOne
	@JoinColumn(name = "url_id")
	public Url getUrl() {
		return url;
	}

	public void setUrl(Url url) {
		this.url = url;
	}

	public String getFromUrl() {
		return fromUrl;
	}

	public void setFromUrl(String fromUrl) {
		this.fromUrl = fromUrl;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getRelativeUrl() {
		return relativeUrl;
	}

	public void setRelativeUrl(String relativeUrl) {
		this.relativeUrl = relativeUrl;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getPublishDateTime() {
		return publishDateTime;
	}

	public void setPublishDateTime(Date publishDateTime) {
		this.publishDateTime = publishDateTime;
	}

	public String getSubjUrl() {
		return subjUrl;
	}

	public void setSubjUrl(String subjUrl) {
		this.subjUrl = subjUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCatchTime() {
		return catchTime;
	}

	public void setCatchTime(Date catchTime) {
		this.catchTime = catchTime;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("fromUrl=").append(fromUrl).append(";relativeUrl=").append(
				relativeUrl).append(";subject=").append(subject).append(
				";publishDate=").append(publishDate)
				.append(";publishDateTime=").append(publishDateTime).append(
						";subjUrl=").append(subjUrl).append(";content=")
				.append(content).append(";catchTime=").append(catchTime)
				.append(";").append(url.toString()).toString();
		return buf.toString();
	}
}
