package org.springside.examples.quickstart.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//JPA标识
@Entity
@Table(name = "pms_task")
public class CatchTask implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6150450030078651624L;

	protected Long id;

	private String code;

	private String name;

	/**
	 * 抓取类型
	 */
	private String catchType;

	/**
	 * 抓取频率
	 */
	private String cron;

	/**
	 * 匹配的抓取网址 按分组/按分级/按网址编码匹配/直接写like的条件
	 * 
	 */
	private String urlRule;

	private String description;

	/**
	 * 最近一次抓取开始时间
	 */
	private Date startDate;

	/**
	 * 最近一次抓取结束时间
	 */
	private Date endDate;

	/**
	 * 最近一次持续时间(秒数)
	 */
	private double duration;

	/**
	 * 最近一次抓取状态：SUCCESS成功/失败FAIL
	 */
	private String status;

	/**
	 * 匹配邮箱
	 */
	private String emailRule;

	/**
	 * 默认抓取几天之内的数据，默认3天
	 */
	private String days;

	/**
	 * 关键字过滤 通过#分隔
	 */
	private String keyWords;

	/**
	 * 任务类型： 抓取任务或邮件任务
	 */
	private String type;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "email_rule")
	public String getEmailRule() {
		return emailRule;
	}

	public void setEmailRule(String emailRule) {
		this.emailRule = emailRule;
	}

	@Column(name = "catch_type")
	public String getCatchType() {
		return catchType;
	}

	public void setCatchType(String catchType) {
		this.catchType = catchType;
	}

	@Column(name = "url_rule")
	public String getUrlRule() {
		return urlRule;
	}

	public void setUrlRule(String urlRule) {
		this.urlRule = urlRule;
	}

	@Column(name = "key_words")
	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	@Column(name = "days")
	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	@Column(name = "start_date")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "end_date")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "duration")
	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "cron")
	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	@Column(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
