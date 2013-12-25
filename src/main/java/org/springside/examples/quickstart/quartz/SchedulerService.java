package org.springside.examples.quickstart.quartz;

import java.util.List;

import org.springside.examples.quickstart.entity.CatchTask;

public interface SchedulerService {
	public final String jobGroup = "COM.BJM.PUB.COMPONENT.QUARTZ";
	public final String CATCHTASKINFO = "URL_RULES";
	public final String URLLIST = "URLLIST";
	public final String EMAILLIST = "EMAILLIST";
	public final String CATCHSERVICE = "CATCH_SERVICE";
	public final String SUBJECTSERVICE = "SUBJECT_SERVICE";

	/**
	 * @param tasks
	 */
	void schedule(List<CatchTask> tasks);

	/**
	 * key jobDetail value cronExpression
	 * 
	 * @param jobsMap
	 */
	// void schedule(Map<JobDetail, String> jobsMap);

	/**
	 * 根据 Quartz Cron Expression 调试任务
	 * 
	 * @param cronExpression
	 *            Quartz Cron 表达式，如 "0/10 * * ? * * *"等
	 */
	// void schedule(String cronExpression);

	/**
	 * 根据 Quartz Cron Expression 调试任务
	 * 
	 * @param name
	 *            Quartz CronTrigger名称
	 * @param cronExpression
	 *            Quartz Cron 表达式，如 "0/10 * * ? * * *"等
	 */
	// void schedule(String name, String cronExpression);

	/**
	 * 根据 Quartz Cron Expression 调试任务
	 * 
	 * @param cronExpression
	 *            Quartz CronExpression
	 */
	// void schedule(CronExpression cronExpression);

	/**
	 * 根据 Quartz Cron Expression 调试任务
	 * 
	 * @param name
	 *            Quartz CronTrigger名称
	 * @param cronExpression
	 *            Quartz CronExpression
	 */
	// void schedule(CronExpression cronExpression, JobDetail job);

	/**
	 * 在startTime时执行调试一次
	 * 
	 * @param startTime
	 *            调度开始时间
	 */
	// void schedule(Date startTime);

	/**
	 * 在startTime时执行调试一次
	 * 
	 * @param name
	 *            Quartz SimpleTrigger 名称
	 * @param startTime
	 *            调度开始时间
	 */
	// void schedule(String name, Date startTime);

	/**
	 * 在startTime时执行调试，endTime结束执行调度
	 * 
	 * @param startTime
	 *            调度开始时间
	 * @param endTime
	 *            调度结束时间
	 */
	// void schedule(Date startTime, Date endTime);

	/**
	 * 在startTime时执行调试，endTime结束执行调度
	 * 
	 * @param name
	 *            Quartz SimpleTrigger 名称
	 * @param startTime
	 *            调度开始时间
	 * @param endTime
	 *            调度结束时间
	 */
	// void schedule(String name, Date startTime, Date endTime);

	/**
	 * 在startTime时执行调试，endTime结束执行调度，重复执行repeatCount次
	 * 
	 * @param startTime
	 *            调度开始时间
	 * @param endTime
	 *            调度结束时间
	 * @param repeatCount
	 *            重复执行次数
	 */
	// void schedule(Date startTime, Date endTime, int repeatCount);

	/**
	 * 在startTime时执行调试，endTime结束执行调度，重复执行repeatCount次
	 * 
	 * @param name
	 *            Quartz SimpleTrigger 名称
	 * @param startTime
	 *            调度开始时间
	 * @param endTime
	 *            调度结束时间
	 * @param repeatCount
	 *            重复执行次数
	 */
	// void schedule(String name, Date startTime, Date endTime, int repeatCount,
	// JobDetail jobDetail);

	/**
	 * 在startTime时执行调试，endTime结束执行调度，重复执行repeatCount次，每隔repeatInterval秒执行一次
	 * 
	 * @param startTime
	 *            调度开始时间
	 * @param endTime
	 *            调度结束时间
	 * @param repeatCount
	 *            重复执行次数
	 * @param repeatInterval
	 *            执行时间隔间
	 */
	// void schedule(Date startTime, Date endTime, int repeatCount,
	// long repeatInterval, JobDetail jobDetail);

	/**
	 * 在startTime时执行调试，endTime结束执行调度，重复执行repeatCount次，每隔repeatInterval秒执行一次
	 * 
	 * @param name
	 *            Quartz SimpleTrigger 名称
	 * @param startTime
	 *            调度开始时间
	 * @param endTime
	 *            调度结束时间
	 * @param repeatCount
	 *            重复执行次数
	 * @param repeatInterval
	 *            执行时间隔间
	 */
	// void schedule(String name, Date startTime, Date endTime, int repeatCount,
	// long repeatInterval, JobDetail jobDetail);
}
