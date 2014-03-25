package org.springside.examples.quickstart.quartz;

import java.util.List;

import org.springside.examples.quickstart.entity.CatchTask;

public interface SchedulerService {
	public final String CatchType = "CATCH";
	public final String EmailType = "EMAIL";
	
	public final String CATCHSERVICE = "CATCH_SERVICE";
	public final String URLSERVICE = "URL_SERVICE";
	public final String TASKSERVICE = "TASK_SERVICE";
	public final String SUBJECTSERVICE = "SUBJECT_SERVICE";
	public final String EMAILSERVICE = "EMAIL_SERVICE";
	public final String TASKID = "TASKID";
	
	
	
	public final String URLRULE = "TASK_RULE";
	public final String CATCHTASKINFO = "URL_RULES";
	public final String URLLIST = "URLLIST";
	public final String EMAILLIST = "EMAILLIST";
	public final String KEYWORDSLIST = "KEYWORD_LIST";
	public final String FROMEMAIL = "FROM_EMAIL";
	
	public final String ERROR_EMAIL_CODE="ERROR_EMAIL";
	
	void start(List<CatchTask> tasks);
	
	void stop(List<CatchTask> tasks);
}
