package org.springside.examples.quickstart.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

//JPA标识
@Entity
@Table(name = "pms_task")
public class CatchTask extends IdEntity {

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
	 * 关键字过滤
	 * 通过#分隔
	 */
	private String keyWords;
	
	/**
	 * 任务类型：
	 * 抓取任务或邮件任务
	 */
	private String type;
	
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEmailRule() {
		return emailRule;
	}

	public void setEmailRule(String emailRule) {
		this.emailRule = emailRule;
	}

	public String getCatchType() {
		return catchType;
	}

	public void setCatchType(String catchType) {
		this.catchType = catchType;
	}

	public String getUrlRule() {
		return urlRule;
	}

	public void setUrlRule(String urlRule) {
		this.urlRule = urlRule;
	}

	public String getKeyWords() {
		return keyWords;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@NotBlank
	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	// JSR303 BeanValidator的校验规则
	@NotBlank
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
