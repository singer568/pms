package org.springside.examples.quickstart.quartz.job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.domain.Page;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.quickstart.entity.CatchTask;
import org.springside.examples.quickstart.entity.Email;
import org.springside.examples.quickstart.entity.Subjects;
import org.springside.examples.quickstart.quartz.SchedulerService;
import org.springside.examples.quickstart.service.bd.EmailService;
import org.springside.examples.quickstart.service.spider.CatchTaskService;
import org.springside.examples.quickstart.service.spider.SubjectsService;

@Component
@Transactional
public class SendingJob extends QuartzJobBean {

	protected void executeInternal(JobExecutionContext jobexecutioncontext)
			throws JobExecutionException {
		JobDetail job = jobexecutioncontext.getJobDetail();
		JobDataMap map = job.getJobDataMap();

		CatchTaskService taskService = (CatchTaskService) map
				.get(SchedulerService.TASKSERVICE);

		Long taskId = (Long) map.get(SchedulerService.TASKID);

		CatchTask task = taskService.getCatchTask(taskId);

		EmailService emailService = (EmailService) map
				.get(SchedulerService.EMAILSERVICE);
		SubjectsService subjectService = (SubjectsService) map
				.get(SchedulerService.SUBJECTSERVICE);

		String[] mails = task.getEmailRule().split("#");// 以#分隔的多个邮箱

		for (int i = 0; i < mails.length; i++) {
			// 取得待发送的主题
			List<Subjects> subjects = querySubjects(subjectService, mails[i],
					task);
			if (subjects == null || subjects.size() < 1) {
				continue;
			}
			// 发送到指定邮箱
			try {
				sendMail(emailService, subjects, mails[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 更新subject的发送状态并更新数据库
			updateSendedEmail(subjectService, subjects, mails[i]);

		}

	}

	private void sendMail(EmailService emailService, List<Subjects> subjects,
			String mail) throws Exception {

		Email fromMail = emailService.getAllEmail().get(0);
		JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
		senderImpl.setHost(fromMail.getHost());
		MimeMessage mailMessage = senderImpl.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,
				true, "utf-8");
		// 设置收件人，寄件人
		messageHelper.setTo(mail);
		messageHelper.setFrom(fromMail.getEmail());// "tomcat0506@163.com"
		messageHelper.setSubject("最新政策信息");// "测试邮件中上传附件!！"
		messageHelper.setText(getContent(emailService, subjects), true);
		senderImpl.setUsername(fromMail.getUserName());
		senderImpl.setPassword(fromMail.getPwd());
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.timeout", "25000");
		senderImpl.setJavaMailProperties(prop);
		senderImpl.send(mailMessage);

	}

	private void updateSendedEmail(SubjectsService subjectService,
			List<Subjects> subjects, String mail) {
		for (int i = 0; i < subjects.size(); i++) {
			String sendedMail = subjects.get(i).getSendedMail();
			if (null == sendedMail || "".equals(sendedMail.trim())) {
				subjects.get(i).setSendedMail(mail);
			} else {
				subjects.get(i).setSendedMail(sendedMail + "#" + mail);
			}
		}
		subjectService.saveAll(subjects);
	}

	private String getContent(EmailService emailService, List<Subjects> lst) {
		StringBuffer mainMsg = new StringBuffer();

		mainMsg
				.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\" /><style>.table-d table{ background:#F00}  .table-d table td{ background:#FFF} </style></head><body>");
		mainMsg.append("<h1>最新政策信息如下：</h1><br>");
		mainMsg
				.append("<div class=\"table-d\"><table width=\"1200\" border=\"0\" cellspacing=\"1\" cellpadding=\"0\"><tr><td width=\"50%\" align='center' style=\"font-weight:bold;font-size:18px;font-family:'微软雅黑';\">文章标题</td><td width=\"15%\" align='center' style=\"font-weight:bold;font-size:18px;font-family:'微软雅黑';\">发布时间</td><td width=\"15%\" align='center' style=\"font-weight:bold;font-size:18px;font-family:'微软雅黑';\">部委</td><td width=\"10%\" align='center' style=\"font-weight:bold;font-size:18px;font-family:'微软雅黑';\">子板块</td><td width=\"10%\" align='center' style=\"font-weight:bold;font-size:18px;font-family:'微软雅黑';\">板块</td></tr>");

		for (int i = 0; i < lst.size(); i++) {
			Subjects subj = lst.get(i);

			if (i % 2 == 0) {
				mainMsg
						.append(
								"<tr><td style=\"font-size:16px;font-family:'微软雅黑';\">")
						.append("<a target='_blank' href='")
						.append(subj.getSubjUrl())
						.append("'>")
						.append(subj.getSubject())
						.append(
								"</a></td><td style=\"font-size:16px;font-family:'微软雅黑';\">")
						.append(subj.getPublishDate())
						.append(
								"</td><td style=\"font-size:16px;font-family:'微软雅黑';\">")
						.append(subj.getDepartment())
						.append(
								"</td><td style=\"font-size:16px;font-family:'微软雅黑';\">")
						.append(subj.getSubmodule())
						.append(
								"</td><td style=\"font-size:16px;font-family:'微软雅黑';\">")
						.append(subj.getModule()).append("</td></tr>");

			} else {
				mainMsg
						.append(
								"<tr><td style=\"font-size:16px;font-family:'微软雅黑';background:#EEECEB\">")
						.append("<a target='_blank' href='")
						.append(subj.getSubjUrl())
						.append("'>")
						.append(subj.getSubject())
						.append(
								"</a></td><td style=\"font-size:16px;font-family:'微软雅黑';background:#EEECEB\">")
						.append(subj.getPublishDate())
						.append(
								"</td><td style=\"font-size:16px;font-family:'微软雅黑';background:#EEECEB\">")
						.append(subj.getDepartment())
						.append(
								"</td><td style=\"font-size:16px;font-family:'微软雅黑';background:#EEECEB\">")
						.append(subj.getSubmodule())

						.append(
								"</td><td style=\"font-size:16px;font-family:'微软雅黑';background:#EEECEB\">")
						.append(subj.getModule()).append("</td></tr>");
			}

		}
		mainMsg.append("</table></div>");

		mainMsg.append("</body></html>");
		return mainMsg.toString();
	}

	/**
	 * 
	 * @param urlRule
	 *            以#号分隔的key=value对；例如：Like_code=ZhongYang
	 * @return
	 */
	private List<Subjects> querySubjects(SubjectsService subjectService,
			String email, CatchTask task) {
		String urlRule = task.getUrlRule();
		String days = task.getDays();

		String keys = task.getKeyWords();

		Map<String, Object> param = new HashMap<String, Object>();
		String[] strs = urlRule.split("#");
		for (int i = 0; i < strs.length; i++) {
			String[] values = strs[i].split("=");
			param.put(values[0], values[1]);
		}

		param.put("GT_catchTime", getDate(days));

		Page<Subjects> subjects = subjectService.querySubjectsByParam(param);

		List<Subjects> lst = subjects.getContent();
		if (null == lst || lst.size() < 1) {
			return null;
		}

		List<Subjects> result = new ArrayList<Subjects>();

		for (int i = 0; i < lst.size(); i++) {
			Subjects subj = lst.get(i);
			String mails = subj.getSendedMail();
			if ((mails == null || !mails.contains(email))
					&& isContainKeys(subj, keys)) {
				result.add(subj);
			}
		}

		return result;
	}

	/**
	 * 判断一个subjects是否包含需要过滤的关键字
	 * 
	 * @param subj
	 * @param key
	 * @return
	 */
	private boolean isContainKeys(Subjects subj, String key) {
		if (!"1".equals(subj.getFilter())) { // 不需要过滤
			return true;
		}
		if (key == null || key.trim().length() < 1) {// 关键词为空
			return true;
		}
		String[] keys = key.split("#");
		for (int i = 0; i < keys.length; i++) {
			if (subj.getSubject().contains(keys[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 每次查出几天之内的信息
	 * 
	 * @param days
	 * @return
	 */
	private Date getDate(String days) {
		Integer day = Integer.parseInt(days);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 0 - day.intValue());
		return cal.getTime();
	}

}