package org.springside.examples.quickstart.quartz;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TaskInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String taskName;

	private String cron;

	private Map<String, String> urlRule;

	public TaskInfo(String taskName, String cron) {
		this.taskName = taskName;
		this.cron = cron;
		urlRule = new HashMap<String, String>();
	}

	public void addUrlRule(String url, String rule) {
		urlRule.put(url, rule);
	}

	public String getTaskName() {
		return taskName;
	}

	public String getCron() {
		return cron;
	}

	public Map<String, String> getUrlRule() {
		return urlRule;
	}

}
