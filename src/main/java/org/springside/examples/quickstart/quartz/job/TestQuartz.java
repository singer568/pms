package org.springside.examples.quickstart.quartz.job;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springside.examples.quickstart.entity.CatchTask;
import org.springside.examples.quickstart.quartz.SchedulerService;

public class TestQuartz {
	public static void main(String[] args) {
		ApplicationContext springContext = new ClassPathXmlApplicationContext(
				new String[] { "classpath:applicationContext.xml",
						"classpath:applicationContext-quartz.xml" });

		SchedulerService schedulerService = (SchedulerService) springContext
				.getBean("schedulerService");

		List<CatchTask> lst = new ArrayList<CatchTask>();

		// CatchTask task1 = new CatchTask("task_001", "0/1 * * ? * * *");
		// task1.addUrlRule("http://www.1.com", "1");
		// task1.addUrlRule("http://www.11.com", "11");
		// task1.addUrlRule("http://www.111.com", "111");
		// task1.addUrlRule("http://www.1111.com", "1111");
		// task1.addUrlRule("http://www.11111.com", "11111");
		// lst.add(task1);
		// CatchTask task2 = new CatchTask("task_002", "0/2 * * ? * * *");
		// task2.addUrlRule("http://www.2.com", "2");
		// task2.addUrlRule("http://www.22.com", "22");
		// task2.addUrlRule("http://www.222.com", "222");
		// task2.addUrlRule("http://www.2222.com", "2222");
		// task2.addUrlRule("http://www.22222.com", "22222");
		// lst.add(task2);
		//
		// schedulerService.schedule(lst);
		//
		// // 执行业务逻辑...
		//
		// // 设置调度任务
		// // 每1秒中执行调试一次
		// // schedulerService.schedule("aaa","0/1 * * ? * * *");
		//
		// CatchTask task3 = new CatchTask("task_003", "0/3 * * ? * * *");
		// task3.addUrlRule("http://www.3.com", "3");
		// task3.addUrlRule("http://www.33.com", "33");
		// task3.addUrlRule("http://www.333.com", "333");
		// task3.addUrlRule("http://www.3333.com", "3333");
		// task3.addUrlRule("http://www.33333.com", "33333");
		// lst.add(task3);

		long curr = System.currentTimeMillis();
		while (true) {
			long end = System.currentTimeMillis();
			if (end - curr >= 10000) {
				System.out.println("======================");
				schedulerService.schedule(lst);
				break;
			}
		}

		// Date startTime = parse("2013-11-28 09:29:00");
		// Date endTime = parse("2013-11-28 09:31:00");

		// 2009-06-01 21:50:00开始执行调度
		// schedulerService.schedule(startTime);

		// 2009-06-01 21:50:00开始执行调度，2009-06-01 21:55:00结束执行调试
		// schedulerService.schedule(startTime,endTime);

		// 2009-06-01 21:50:00开始执行调度，执行5次结束
		// schedulerService.schedule(startTime,null,5);

		// 2009-06-01 21:50:00开始执行调度，每隔20秒执行一次，执行5次结束
		// schedulerService.schedule(startTime,null,5,20);

		// 等等，查看com.sundoctor.quartz.service.SchedulerService
	}

	private static Date parse(String dateStr) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

}
