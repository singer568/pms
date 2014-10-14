package org.springside.examples.quickstart.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

//JPA标识
@Entity
@Table(name = "pms_taskhistory")
public class CatchTaskHistory implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3501411372588101101L;

	protected Long id;

	/**
	 * 抓取任务
	 */
	private CatchTask task;

	/**
	 * 抓取开始时间
	 */
	private Date startDate;

	/**
	 * 抓取结束时间
	 */
	private Date endDate;

	/**
	 * 抓取持续时间
	 */
	private String duration;

	/**
	 * 整个抓取状态：成功：SUCCESS，失败：FAIL
	 */
	private String status;

	/**
	 * 整个抓取日志，成功、失败详情
	 */
	private String logInfo;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne
	@JoinColumn(name = "task_id")
	public CatchTask getTask() {
		return task;
	}

	public void setTask(CatchTask task) {
		this.task = task;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "start_date")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	@Column(name = "end_date")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@Column(name = "duration")
	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
	@Column(name = "status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	@Column(name = "logInfo")
	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

}
