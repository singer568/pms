package org.springside.examples.quickstart.quartz;

import java.text.ParseException;
import java.util.List;

import org.quartz.CronExpression;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springside.examples.quickstart.quartz.job.SpiderJob;

@Service("schedulerService")
public class SchedulerServiceImpl implements SchedulerService {

	private Scheduler scheduler;

	@Autowired
	public void setScheduler(@Qualifier("quartzScheduler") Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public void schedule(List<TaskInfo> tasks) {

		deleteAll();

		for (int i = 0, len = tasks.size(); i < len; i++) {
			TaskInfo task = tasks.get(i);

			JobDetail jobDetail = new JobDetail();
			jobDetail.setGroup(jobGroup);
			jobDetail.setName(task.getTaskName());
			jobDetail.getJobDataMap().put(URLRULES, task.getUrlRule());
			
			jobDetail.setJobClass(SpiderJob.class);

			schedule(task.getCron(), jobDetail);
		}
	}

	private void deleteAll() {
		try {
			String[] jobNames = scheduler.getJobNames(jobGroup);
			if (null != jobNames && jobNames.length > 0) {
				for (int i = 0, len = jobNames.length; i < len; i++) {
					scheduler.unscheduleJob(jobNames[i], jobGroup);
					scheduler.deleteJob(jobNames[i], jobGroup);
				}
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}

	// public void schedule(Map<JobDetail, String> jobsMap) {
	// Set<Map.Entry<JobDetail, String>> set = jobsMap.entrySet();
	// Iterator<Map.Entry<JobDetail, String>> it = set.iterator();
	// while (it.hasNext()) {
	// Map.Entry<JobDetail, String> entry = it.next();
	// schedule(entry.getValue(), entry.getKey());
	// }
	//
	// }

	private void schedule(String cron, JobDetail jobDetail) {
		try {
			String name = jobDetail.getName();
			CronExpression cronExpression = new CronExpression(cron);

			scheduler.addJob(jobDetail, true);

			CronTrigger cronTrigger = new CronTrigger(name, jobGroup,
					jobDetail.getName(), jobGroup);

			cronTrigger.setCronExpression(cronExpression);

			scheduler.scheduleJob(cronTrigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	// public void schedule(String name, Date startTime, Date endTime,
	// int repeatCount, long repeatInterval, JobDetail jobDetail) {
	// try {
	// if (name == null || name.trim().equals("")) {
	// throw new RuntimeException("当前要执行的定时任务名称为空，请检查");
	// }
	// if (scheduler.getJobDetail(name, jobGroup) == null) {
	// scheduler.addJob(jobDetail, true);
	// SimpleTrigger simpleTrigger = new SimpleTrigger(name,
	// jobGroup, jobDetail.getName(), jobGroup,
	// startTime, endTime, repeatCount, repeatInterval);
	// scheduler.scheduleJob(simpleTrigger);
	// } else {
	// SimpleTrigger simpleTrigger = new SimpleTrigger(name,
	// jobGroup, jobDetail.getName(), jobGroup,
	// startTime, endTime, repeatCount, repeatInterval);
	// scheduler.rescheduleJob(name, jobGroup, simpleTrigger);
	// }
	// } catch (SchedulerException e) {
	// throw new RuntimeException(e);
	// }
	// }

	// public void schedule(String cronExpression) {
	// schedule(null, cronExpression);
	// }

	// @Override
	// public void schedule(String name, String cronExpression) {
	// try {
	// schedule(name, new CronExpression(cronExpression));
	// } catch (java.text.ParseException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// @Override
	// public void schedule(CronExpression cronExpression) {
	// schedule(null, cronExpression);
	// }
	//
	// @Override
	// public void schedule(Date startTime) {
	// schedule(startTime, null);
	// }
	//
	// @Override
	// public void schedule(String name, Date startTime) {
	// schedule(name, startTime, null);
	// }
	//
	// @Override
	// public void schedule(Date startTime, Date endTime) {
	// schedule(startTime, endTime, 0);
	// }
	//
	// @Override
	// public void schedule(String name, Date startTime, Date endTime) {
	// schedule(name, startTime, endTime, 0);
	// }
	//
	// @Override
	// public void schedule(Date startTime, Date endTime, int repeatCount) {
	// schedule(null, startTime, endTime, 0);
	// }
	//
	// @Override
	// public void schedule(String name, Date startTime, Date endTime,
	// int repeatCount, JobDetail jobDetail) {
	// schedule(name, startTime, endTime, 0, 0L, jobDetail);
	// }
	//
	// @Override
	// public void schedule(Date startTime, Date endTime, int repeatCount,
	// long repeatInterval, JobDetail jobDetail) {
	// schedule(null, startTime, endTime, repeatCount, repeatInterval,
	// jobDetail);
	// }

}