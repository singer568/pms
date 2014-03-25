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

	private CatchTask task;

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
	private String publishDate;

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
	private String catchTime;

	/**
	 * 是否为关键主题 1:为关键 0:非关键
	 */
	private String keyFlag;

	/**
	 * 删除标志
	 */
	private int dr;

	private Level level;

	private Group group;

	/**
	 * 已发送邮箱，中间用#号分隔
	 */
	private String sendedMail;

	/**
	 * 抓取来源网址
	 */
	private String fromUrl;

	private String code;
	private String name;

	/**
	 * 省份，中央直属的写“中央”
	 */
	private String province;
	/**
	 * 部委：科技部、农业部等
	 */
	private String department;
	/**
	 * 板块
	 */
	private String module;
	/**
	 * 子版块
	 */
	private String submodule;

	/**
	 * 中部：MIDDLE；东部：EAST：西部：WEST；
	 */
	private String area;

	/**
	 * 是否为省会：1：是；0：否
	 */
	private String isCapital;
	/**
	 * 是否按关键词筛选：0：否；1：是
	 */
	private String filter;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getSubmodule() {
		return submodule;
	}

	public void setSubmodule(String submodule) {
		this.submodule = submodule;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getIsCapital() {
		return isCapital;
	}

	public void setIsCapital(String isCapital) {
		this.isCapital = isCapital;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getSendedMail() {
		return sendedMail;
	}

	public void setSendedMail(String sendedMail) {
		this.sendedMail = sendedMail;
	}

	public String getKeyFlag() {
		return keyFlag;
	}

	public void setKeyFlag(String keyFlag) {
		this.keyFlag = keyFlag;
	}

	@ManyToOne
	@JoinColumn(name = "task_id")
	public CatchTask getTask() {
		return task;
	}

	public void setTask(CatchTask task) {
		this.task = task;
	}

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

	public String getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(String publishDate) {
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

	public String getCatchTime() {
		return catchTime;
	}

	public void setCatchTime(String catchTime) {
		this.catchTime = catchTime;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("fromUrl=").append(fromUrl).append(";subject=").append(
				subject).append("<br/>").toString();
		return buf.toString();
	}
}
