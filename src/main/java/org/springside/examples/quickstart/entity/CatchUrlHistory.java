package org.springside.examples.quickstart.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//JPA标识
@Entity
@Table(name = "pms_urlhistory")
public class CatchUrlHistory extends IdEntity {

	/**
	 * 抓取开始时间
	 */
	private String startDate;

	/**
	 * 抓取结束时间
	 */
	private String endDate;

	/**
	 * 抓取持续时间
	 */
	private double duration;

	/**
	 * 整个抓取状态：成功：SUCCESS，失败：FAIL
	 */
	private String status;

	/**
	 * 所属抓取任务
	 */
	private CatchTask task;

	/**
	 * 抓取到的主题；用#号分隔开
	 */
	private String subjects;
	
	/**
	 * 对应一次抓取日志
	 */
	private CatchTaskHistory taskHistory;

	/**
	 * 抓取的网址
	 */
	private Url url;

	@ManyToOne
	@JoinColumn(name = "task_history_id")
	public CatchTaskHistory getTaskHistory() {
		return taskHistory;
	}

	public void setTaskHistory(CatchTaskHistory taskHistory) {
		this.taskHistory = taskHistory;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getSubjects() {
		return subjects;
	}

	public void setSubjects(String subjects) {
		this.subjects = subjects;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
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

	@ManyToOne
	@JoinColumn(name = "task_id")
	public CatchTask getTask() {
		return task;
	}

	public void setTask(CatchTask task) {
		this.task = task;
	}

	@ManyToOne
	@JoinColumn(name = "url_id")
	public Url getUrl() {
		return url;
	}

	public void setUrl(Url url) {
		this.url = url;
	}

}
