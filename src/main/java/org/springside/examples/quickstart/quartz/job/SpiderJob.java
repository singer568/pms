package org.springside.examples.quickstart.quartz.job;

import java.util.Map;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springside.examples.quickstart.quartz.SchedulerService;

public class SpiderJob extends QuartzJobBean {

	protected void executeInternal(JobExecutionContext jobexecutioncontext)
			throws JobExecutionException {
		JobDetail job = jobexecutioncontext.getJobDetail();

		JobDataMap map = job.getJobDataMap();
		Object obj = map.get(SchedulerService.URLRULES);

		if (null == obj) {
			throw new RuntimeException("没找到要抓取的网址及规则");
		}
		catchPolicyInfo((Map<String, String>) obj);
	}

	private void catchPolicyInfo(Map<String, String> map) {
		System.out.println(map);
		
	}

}