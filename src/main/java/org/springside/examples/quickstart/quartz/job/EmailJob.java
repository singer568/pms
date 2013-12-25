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
import org.springside.examples.quickstart.entity.Email;
import org.springside.examples.quickstart.quartz.SchedulerService;
import org.springside.examples.quickstart.service.spider.SubjectsService;

@Component
@Transactional
public class EmailJob extends QuartzJobBean {

	protected void executeInternal(JobExecutionContext jobexecutioncontext)
			throws JobExecutionException {
		JobDetail job = jobexecutioncontext.getJobDetail();
		JobDataMap map = job.getJobDataMap();
		Object emailList = map.get(SchedulerService.EMAILLIST);	
		Object subjectService = map.get(SchedulerService.SUBJECTSERVICE);

		if (null == emailList) {
			throw new RuntimeException("没找到要发送的邮件地址");
		}
		sendEmail((List<Email>) emailList, (SubjectsService)subjectService);
	}

	private void sendEmail(List<Email> emailList, SubjectsService subjectService) {
		Iterator<Email> it = emailList.iterator();
		while (it.hasNext()) {
//			Url url = it.next();
//			List<Subjects> subjs = catchService.catchPolicy(url);
		}
	}

}