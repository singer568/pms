package org.springside.examples.quickstart.quartz.job;

import java.util.Iterator;
import java.util.List;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.quickstart.entity.Subjects;
import org.springside.examples.quickstart.entity.Url;
import org.springside.examples.quickstart.quartz.SchedulerService;
import org.springside.examples.quickstart.service.spider.util.CatchService;

@Component
@Transactional
public class SpiderJob extends QuartzJobBean {

	protected void executeInternal(JobExecutionContext jobexecutioncontext)
			throws JobExecutionException {
		JobDetail job = jobexecutioncontext.getJobDetail();

		JobDataMap map = job.getJobDataMap();
		Object urlList = map.get(SchedulerService.URLLIST);
		Object catchService = map.get(SchedulerService.CATCHSERVICE);

		if (null == urlList) {
			throw new RuntimeException("没找到要抓取的任务信息");
		}
		catchPolicyInfo((List<Url>) urlList, (CatchService) catchService);
	}

	private void catchPolicyInfo(List<Url> urlList, CatchService catchService) {
		Iterator<Url> it = urlList.iterator();
		while (it.hasNext()) {
			Url url = it.next();
			List<Subjects> subjs = catchService.catchPolicy(url);
		}
	}

}