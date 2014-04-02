package org.springside.examples.quickstart.quartz.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.quickstart.entity.CatchTask;
import org.springside.examples.quickstart.entity.Email;
import org.springside.examples.quickstart.entity.Subjects;
import org.springside.examples.quickstart.entity.Url;
import org.springside.examples.quickstart.quartz.SchedulerService;
import org.springside.examples.quickstart.service.bd.EmailService;
import org.springside.examples.quickstart.service.spider.CatchTaskService;
import org.springside.examples.quickstart.service.spider.UrlService;
import org.springside.examples.quickstart.service.spider.util.CatchService;

@Component
@Transactional
public class SpiderJob extends QuartzJobBean {

	protected void executeInternal(JobExecutionContext jobexecutioncontext)
			throws JobExecutionException {
		JobDetail job = jobexecutioncontext.getJobDetail();

		JobDataMap map = job.getJobDataMap();

		CatchService catchService = (CatchService) map
				.get(SchedulerService.CATCHSERVICE);
		UrlService urlService = (UrlService) map
				.get(SchedulerService.URLSERVICE);

		CatchTaskService taskService = (CatchTaskService) map
				.get(SchedulerService.TASKSERVICE);

		Long taskId = (Long) map.get(SchedulerService.TASKID);

		CatchTask task = taskService.getCatchTask(taskId);

		EmailService emailService = (EmailService) map
				.get(SchedulerService.EMAILSERVICE);

		List<Subjects> subjects = new ArrayList<Subjects>();
		List<Url> urlList = getUrls(urlService, task.getUrlRule());// 取得需要抓取的Url
		Iterator<Url> it = urlList.iterator();

		List<Url> errors = new ArrayList<Url>();// 存储本次抓取失败的URL
		List<Url> success = new ArrayList<Url>();// 存储本次抓取成功的URL

		while (it.hasNext()) {
			List<Subjects> lst = new ArrayList<Subjects>();
			Url url = it.next();
			try {
				lst = catchService.catchPolicy(url);
				subjects.addAll(lst);

				url.setValid("1");
				url.setSuccess("1");
				url.setCatchTime(getCurrentDate());
				url.setErrMsg(null);
				success.add(url);
			} catch (Exception e) {
				url.setValid("0");
				url.setSuccess("0");
				url.setCatchTime(getCurrentDate());
				url.setErrMsg(getErrorMsg(url, job, e));
				errors.add(url);
				e.printStackTrace();
			}
		}

		if (errors.size() > 0) {
			try {
				sendEmail(emailService, getContent(errors), "抓取失败网址");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		String updateErrMsg = "";
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < subjects.size(); i++) {
			try {
				catchService.updateSubject(subjects.get(i));
			} catch (Exception e) {
				if (buf.length() < 1) {
					buf.append("<br/><h1>保存失败的主题：</h1>");
				}
				buf
						.append("<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=2)\" width=\"100%\" color=#FF0000 SIZE=5>");
				buf.append(subjects.get(i));
				buf.append("<br/>").append("<h1>错误消息如下</h1>：<br/>");
				buf.append(e.getMessage()).append("<br/><h1>堆栈详情如下：</h1><br/>")
						.append(getStackTraceStr(e.getStackTrace()));
				e.printStackTrace();
			}
		}

		StringBuffer buf1 = new StringBuffer();
		try {
			urlService.saveUrls(errors);
			urlService.saveUrls(success);
		} catch (Exception e) {
			buf1.append("<br/><h1>更新成功/失败url信息失败").append("</h1>");
			buf1.append("<br/><h1>抓取失败网址：</h1>").append(errors);
			buf1.append("<br/><h1>抓取成功网址：</h1>").append(success);
			buf1.append("<br/>").append("<h1>错误消息如下</h1>：<br/>");
			buf1.append(e.getMessage()).append("<br/><h1>堆栈详情如下：</h1><br/>")
					.append(getStackTraceStr(e.getStackTrace()));
			e.printStackTrace();
		}
		if (buf.length() > 0)
			updateErrMsg = buf.toString();
		if (buf1.length() > 0)
			updateErrMsg = updateErrMsg + buf1.toString();

		if (updateErrMsg != null && !"".equals(updateErrMsg.trim())) {
			try {
				sendEmail(emailService, updateErrMsg, "更新主题，url有效信息失败");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private String getContent(List<Url> errors) {
		StringBuffer mainMsg = new StringBuffer();
		StringBuffer detailMsg = new StringBuffer();

		mainMsg
				.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\" /><style>.table-d table{ background:#F00}  .table-d table td{ background:#FFF} </style></head><body>");
		mainMsg.append("<h1>失败网址如下：</h1><br>");
		mainMsg
				.append("<div class=\"table-d\"><table width=\"1200\" border=\"0\" cellspacing=\"1\" cellpadding=\"0\"><tr><td width=\"10%\" align='center' style=\"font-weight:bold;font-size:18px;font-family:'微软雅黑';\">编码/名称</td><td width=\"10%\" align='center' style=\"font-weight:bold;font-size:18px;font-family:'微软雅黑';\">部委</td><td width=\"15%\" align='center' style=\"font-weight:bold;font-size:18px;font-family:'微软雅黑';\">子板块</td><td width=\"15%\" align='center' style=\"font-weight:bold;font-size:18px;font-family:'微软雅黑';\">板块</td><td width=\"10%\" align='center' style=\"font-weight:bold;font-size:18px;font-family:'微软雅黑';\">抓取时间</td><td width=\"40%\" align='center' style=\"font-weight:bold;font-size:18px;font-family:'微软雅黑';\">网址</td></tr>");

		for (int i = 0; i < errors.size(); i++) {
			Url url = errors.get(i);

			if (i % 2 == 0) {
				mainMsg
						.append(
								"<tr><td style=\"font-size:16px;font-family:'微软雅黑';\">")
						.append(url.getCode())
						.append("/")
						.append(url.getName())
						.append(
								"</td><td style=\"font-size:16px;font-family:'微软雅黑';\">")
						.append(url.getDepartment())
						.append(
								"</td><td style=\"font-size:16px;font-family:'微软雅黑';\">")
						.append(url.getSubmodule())
						.append(
								"</td><td style=\"font-size:16px;font-family:'微软雅黑';\">")
						.append(url.getModule())
						.append(
								"</td><td style=\"font-size:16px;font-family:'微软雅黑';\">")
						.append(url.getCatchTime())
						.append(
								"</td><td style=\"font-size:16px;font-family:'微软雅黑';\">")
						.append(url.getUrl()).append("</td></tr>");
			} else {
				mainMsg
						.append(
								"<tr><td style=\"font-size:16px;font-family:'微软雅黑';\">")
						.append(url.getCode())
						.append("/")
						.append(url.getName())
						.append(
								"</td><td style=\"font-size:16px;font-family:'微软雅黑';background:#EEECEB\">")
						.append(url.getDepartment())
						.append(
								"</td><td style=\"font-size:16px;font-family:'微软雅黑';background:#EEECEB\">")
						.append(url.getSubmodule())
						.append(
								"</td><td style=\"font-size:16px;font-family:'微软雅黑';background:#EEECEB\">")
						.append(url.getModule())
						.append(
								"</td><td style=\"font-size:16px;font-family:'微软雅黑';background:#EEECEB\">")
						.append(url.getCatchTime())

						.append(
								"</td><td style=\"font-size:16px;font-family:'微软雅黑';background:#EEECEB\">")
						.append(url.getUrl()).append("</td></tr>");
			}

			detailMsg.append("<br>").append(url.getErrMsg());

		}
		mainMsg.append("</table></div><br>").append("<h1>详情如下：</h1>").append(
				detailMsg);

		mainMsg.append("</body></html>");
		return mainMsg.toString();
	}

	private String getErrorMsg(Url url, JobDetail job, Exception e) {
		StringBuffer buf = new StringBuffer();
		buf
				.append("<HR style=\"FILTER: alpha(opacity=100,finishopacity=0,style=2)\" width=\"100%\" color=#FF0000 SIZE=5>");
		buf.append("<br/><h1>错误网址：</h1>").append(url.toString());
		buf.append("<br/>").append("<h1>错误消息如下</h1>：<br/>");
		buf.append(e.getMessage()).append("<br/><h1>堆栈详情如下：</h1><br/>").append(
				getStackTraceStr(e.getStackTrace()));
		return buf.toString();
	}

	private String getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(cal.getTime());
	}

	private String getStackTraceStr(StackTraceElement[] elements) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < elements.length; i++) {
			buf.append("<br/>    " + elements[i].getClassName() + "."
					+ elements[i].getMethodName() + "("
					+ elements[i].getFileName() + ":"
					+ elements[i].getLineNumber() + ")");
		}
		return buf.toString();
	}

	private void sendEmail(EmailService emailService, String errorMsg,
			String subject) throws Exception {
		Email mail = emailService.getAllEmail().get(0);
		JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
		senderImpl.setHost(mail.getHost());
		MimeMessage mailMessage = senderImpl.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,
				true, "utf-8");
		// 设置收件人，寄件人
		messageHelper.setTo(mail.getErrEmail().split("#"));
		messageHelper.setFrom(mail.getEmail());// "tomcat0506@163.com"
		messageHelper.setSubject(subject + "（" + getCurrentDate() + "）");// "测试邮件中上传附件!！"
		messageHelper.setText(errorMsg, true);
		senderImpl.setUsername(mail.getUserName());
		senderImpl.setPassword(mail.getPwd());
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.timeout", "25000");
		senderImpl.setJavaMailProperties(prop);
		senderImpl.send(mailMessage);
	}

	/**
	 * 
	 * @param urlRule
	 *            以#号分隔的key=value对；例如：Like_code=ZhongYang
	 * @return
	 */
	private List<Url> getUrls(UrlService urlService, String urlRule) {
		Map<String, Object> param = new HashMap<String, Object>();
		String[] strs = urlRule.split("#");
		for (int i = 0; i < strs.length; i++) {
			String[] values = strs[i].split("=");
			param.put(values[0], values[1]);
		}
		List<Url> urls = urlService.queryUrlByParam(param);
		return urls;
	}

}