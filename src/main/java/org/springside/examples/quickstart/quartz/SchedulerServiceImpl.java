package org.springside.examples.quickstart.quartz;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.quickstart.entity.CatchTask;
import org.springside.examples.quickstart.entity.Url;
import org.springside.examples.quickstart.quartz.job.SendingJob;
import org.springside.examples.quickstart.quartz.job.SpiderJob;
import org.springside.examples.quickstart.service.bd.EmailService;
import org.springside.examples.quickstart.service.bd.KeyWordsService;
import org.springside.examples.quickstart.service.spider.CatchTaskService;
import org.springside.examples.quickstart.service.spider.SubjectsService;
import org.springside.examples.quickstart.service.spider.UrlService;
import org.springside.examples.quickstart.service.spider.util.CatchService;

@Component
@Transactional
public class SchedulerServiceImpl implements SchedulerService {

	private Scheduler scheduler;

	private CatchTaskService taskService;

	private UrlService urlService;

	private EmailService emailService;

	private CatchService html;

	private SubjectsService subjectService;

	private KeyWordsService wordService;

	public void stop(List<CatchTask> tasks) {
		deleteJobs(tasks);
	}

	public void start(List<CatchTask> tasks) {

		deleteJobs(tasks);

		for (int i = 0, len = tasks.size(); i < len; i++) {
			CatchTask task = tasks.get(i);

			JobDetail jobDetail = new JobDetail();
			jobDetail.setName(task.getCode() + "_" + task.getName());
			jobDetail.setGroup(task.getType());

			if (CatchType.equals(task.getType())) {
				jobDetail.setJobClass(SpiderJob.class);
			} else {
				jobDetail.setJobClass(SendingJob.class);
			}

			jobDetail.getJobDataMap().put(TASKID, task.getId());
			jobDetail.getJobDataMap().put(CATCHSERVICE, html);
			jobDetail.getJobDataMap().put(URLSERVICE, urlService);
			jobDetail.getJobDataMap().put(TASKSERVICE, taskService);
			jobDetail.getJobDataMap().put(EMAILSERVICE, emailService);
			jobDetail.getJobDataMap().put(SUBJECTSERVICE, subjectService);

			schedule(task.getCron(), jobDetail, task.getType());
		}
	}

	public KeyWordsService getWordService() {
		return wordService;
	}

	@Autowired
	public void setWordService(KeyWordsService wordService) {
		this.wordService = wordService;
	}

	public SubjectsService getSubjectService() {
		return subjectService;
	}

	@Autowired
	public void setSubjectService(SubjectsService subjectService) {
		this.subjectService = subjectService;
	}

	public CatchService getHtml() {
		return html;
	}

	public EmailService getEmailService() {
		return emailService;
	}

	@Autowired
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	@Autowired
	public void setHtml(CatchService html) {
		this.html = html;
	}

	public UrlService getUrlService() {
		return urlService;
	}

	@Autowired
	public void setUrlService(UrlService urlService) {
		this.urlService = urlService;
	}

	public CatchTaskService getTaskService() {
		return taskService;
	}

	@Autowired
	public void setTaskService(CatchTaskService taskService) {
		this.taskService = taskService;
	}

	@Autowired
	public void setScheduler(@Qualifier("quartzScheduler") Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * 仅支持 IN_code=('a','v')类型的匹配;
	 * 
	 * @param emailRule
	 * @return
	 */
	private List<String> getEmails(String emailRule) {
		if (null == emailRule || "".equals(emailRule.trim())) {
			return null;
		}
		String[] emails = emailRule.split(";");
		List<String> lst = new ArrayList<String>();
		for (int i = 0; i < emails.length; i++) {
			lst.add(emails[i]);
		}

		return lst;
	}

	private List<Url> getTaskUrls(CatchTask task) {
		String urlRule = task.getUrlRule();
		Map<String, Object> param = new HashMap<String, Object>();

		// Param是以#号分隔的key=value对；例如：Like_code=ZhongYang
		String[] strs = urlRule.split("#");
		for (int i = 0; i < strs.length; i++) {
			String[] values = strs[i].split("=");
			param.put(values[0], values[1]);
		}
		Page<Url> urls = urlService.queryUrlByParam(param);
		return urls.getContent();
	}

	private void deleteJobs(List<CatchTask> tasks) {
		try {
			CatchTask task = null;
			for (int i = 0; i < tasks.size(); i++) {
				task = tasks.get(i);
				String jobName = task.getCode() + "_" + task.getName();
				scheduler.unscheduleJob(jobName, task.getType());
				scheduler.deleteJob(jobName, task.getType());
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}

	private void schedule(String cron, JobDetail jobDetail, String group) {
		try {
			String name = jobDetail.getName();
			CronExpression cronExpression = new CronExpression(cron);

			scheduler.addJob(jobDetail, true);

			CronTrigger cronTrigger = new CronTrigger(name, group, jobDetail
					.getName(), group);

			cronTrigger.setCronExpression(cronExpression);

			scheduler.scheduleJob(cronTrigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}
}