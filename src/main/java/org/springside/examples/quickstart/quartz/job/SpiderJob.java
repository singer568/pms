package org.springside.examples.quickstart.quartz.job;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.quickstart.entity.Email;
import org.springside.examples.quickstart.entity.KeyWords;
import org.springside.examples.quickstart.entity.Subjects;
import org.springside.examples.quickstart.entity.Url;
import org.springside.examples.quickstart.quartz.SchedulerService;
import org.springside.examples.quickstart.service.spider.util.CatchService;
import org.springside.examples.quickstart.service.spider.util.DateUtil;

@Component
@Transactional
public class SpiderJob extends QuartzJobBean {

	private static Logger log = Logger.getLogger(SpiderJob.class.getName());

	public static void main(String[] args) throws IOException {
		String storeDirectory = "D:/抓取任务/" + "任务一" + "/"
				+ DateUtil.getCurrentDateStr() + "/Excel";
		String mhtDirectory = "D:/抓取任务/" + "任务一" + "/"
				+ DateUtil.getCurrentDateStr() + "/Mht";
		File file = new File(storeDirectory);
		if (!file.exists()) {
			file.mkdirs();
		}

		File file2 = new File(mhtDirectory);
		if (!file2.exists()) {
			file2.mkdirs();
		}

		String fileName = storeDirectory + "/" + System.currentTimeMillis()
				+ ".xlsx";
		File file1 = new File(fileName);
		if (!file1.exists()) {
			file1.createNewFile();
		}
	}

	// 增加catchtask参数，明天来了再梳理一下容错信息
	protected void executeInternal(JobExecutionContext jobexecutioncontext)
			throws JobExecutionException {
		JobDetail job = jobexecutioncontext.getJobDetail();

		String excelDirectory = "D:/抓取记录/" + job.getName() + "/"
				+ DateUtil.getCurrentDateStr() + "/Excel";
		File file = new File(excelDirectory);
		if (!file.exists()) {
			file.mkdirs();
		}
		JobDataMap map = job.getJobDataMap();
		Object urlList = map.get(SchedulerService.URLLIST);
		Object catchService = map.get(SchedulerService.CATCHSERVICE);
		Object keyWords = map.get(SchedulerService.KEYWORDSLIST);// 查出所有关键词

		String fileName = excelDirectory + "/" + System.currentTimeMillis()
				+ ".xls";

		Email fromEmail = (Email) map.get(SchedulerService.FROMEMAIL);
		List<Subjects> lst = null;
		List<KeyWords> keys = null;
		try {

			// 只是抓取来了，尚未保存
			lst = catchPolicyInfo((List<Url>) urlList,
					(CatchService) catchService, (List<KeyWords>) keyWords);

			if (null == lst || lst.size() <= 0
					|| lst.get(0).getSubject() == null
					|| "".equals(lst.get(0).getSubject().trim())) {
				return;
			}
			// 同时生成excel文件，并将excel文件发送到对应邮箱中
			writeExcel(lst, fileName);

			Object emailList = map.get(SchedulerService.EMAILLIST);
			List<String> to = (List) emailList;
			Object keyList = map.get(SchedulerService.KEYWORDSLIST);

			keys = (List) keyList;

			if (to != null && to.size() > 0) {
				String[] tos = new String[to.size()];
				to.toArray(tos);
				sendEmail(fromEmail, tos, fileName, job.getName(), lst, keys);
			}
			((CatchService) catchService).updateSubjects(lst);
		} catch (Exception e) {
			try {
				Email tmp = fromEmail.clone();
				tmp.setSubject("执行抓取任务{" + job.getName() + "}出错，请及时处理。");

				StringBuffer buf = new StringBuffer();
				buf.append("<h1>fileName=</h1>").append(fileName).append(
						"<br/><h1>jobName=</h1>").append(job.getName()).append(
						"<br/><h1>urlList=</h1>").append(urlList).append(
						"<br/><h1>keyWords=</h1>").append(keyWords);
				buf.append("<br/><h1>fromEmail=</h1>").append(tmp.toString())
						.append("<br/>").append("<h1>详细错误消息如下</h1>：<br/>");
				buf.append(e.getMessage()).append("<br/><h1>堆栈详情如下：</h1><br/>")
						.append(getStackTraceStr(e.getStackTrace()));
				tmp.setEmailContent(buf.toString());

				sendEmail(tmp, new String[] { "37731465@qq.com",
						"1196233819@qq.com" }, fileName, job.getName(), lst,
						keys);
				e.printStackTrace();

			} catch (Exception e1) {
				e1.printStackTrace();
				throw new JobExecutionException(e1.getCause());
			}
			throw new JobExecutionException(e.getCause());
		}
	}

	private String getStackTraceStr(StackTraceElement[] elements) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < elements.length; i++) {
			buf.append("/n    " + elements[i].getClassName() + "."
					+ elements[i].getMethodName() + "("
					+ elements[i].getFileName() + ":"
					+ elements[i].getLineNumber() + ")");
		}
		return buf.toString();
	}

	private void sendEmail(Email from, String[] to, String fileName,
			String jobName, List<Subjects> lst, List<KeyWords> keys)
			throws Exception {
		JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();

		// 设定mail server
		senderImpl.setHost(from.getHost());// "smtp.163.com"
		// 建立邮件消息,发送简单邮件和html邮件的区别
		MimeMessage mailMessage = senderImpl.createMimeMessage();
		// 注意这里的boolean,等于真的时候才能嵌套图片，在构建MimeMessageHelper时候，所给定的值是true表示启用，
		// multipart模式 为true时发送附件 可以设置html格式
		MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,
				true, "utf-8");

		// 设置收件人，寄件人
		messageHelper.setTo(to);
		messageHelper.setFrom(from.getEmail());// "tomcat0506@163.com"
		messageHelper.setSubject(from.getSubject());// "测试邮件中上传附件!！"
		// true 表示启动HTML格式的邮件

		if (null == keys || keys.size() <= 0) {
			messageHelper.setText(isNull(from.getEmailContent()) ? getContent(
					lst, jobName) : from.getEmailContent(), true);
		} else {
			messageHelper.setText(isNull(from.getEmailContent()) ? getContent(
					getKeyList(lst), jobName) : from.getEmailContent(), true);
		}

		if (null != fileName && !"".equals(fileName.trim())) {
			File file1 = new File(fileName);
			if (file1.exists()) {
				FileSystemResource file = new FileSystemResource(file1);// "d:/项目估算表.xlsx"

				// 这里的方法调用和插入图片是不同的。
				messageHelper.addAttachment(fileName.substring(fileName
						.lastIndexOf("/") + 1), file);
			}
		}

		senderImpl.setUsername(from.getUserName()); // 根据自己的情况,设置username
		// "tomcat0506@163.com"
		senderImpl.setPassword(from.getPwd()); // 根据自己的情况, 设置password
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
		prop.put("mail.smtp.timeout", "25000");
		senderImpl.setJavaMailProperties(prop);
		// 发送邮件
		senderImpl.send(mailMessage);

	}

	private boolean isNull(String str) {
		if (null == str || "".equals(str.trim())) {
			return true;
		}
		return false;
	}

	private String getContent(List<Subjects> lst, String jobName) {
		StringBuffer buf = new StringBuffer();
		buf
				.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\" /><style>.table-d table{ background:#F00}  .table-d table td{ background:#FFF} </style></head><body>");
		buf.append("<h1>(").append(jobName).append(")最新政策信息如下：")
				.append("</h1>");
		if (null != lst && lst.size() > 0) {
			buf
					.append("<div class=\"table-d\"><table width=\"1200\" border=\"0\" cellspacing=\"1\" cellpadding=\"0\"><tr><td width=\"10%\" align='center' style=\"font-weight:bold;font-size:18px;font-family:'微软雅黑';\">部委</td><td width=\"15%\" align='center' style=\"font-weight:bold;font-size:18px;font-family:'微软雅黑';\">子板块</td><td width=\"15%\" align='center' style=\"font-weight:bold;font-size:18px;font-family:'微软雅黑';\">板块</td><td width=\"10%\" align='center' style=\"font-weight:bold;font-size:18px;font-family:'微软雅黑';\">发布日期</td><td width=\"50%\" align='center' style=\"font-weight:bold;font-size:18px;font-family:'微软雅黑';\">文章标题</td></tr>");
			for (int i = 0; i < lst.size(); i++) {
				Subjects subj = lst.get(i);
				if (i % 2 == 0) {
					buf
							.append(
									"<tr><td style=\"font-size:16px;font-family:'微软雅黑';\">")
							.append(subj.getUrl().getDepartment())
							.append(
									"</td><td style=\"font-size:16px;font-family:'微软雅黑';\">")
							.append(subj.getUrl().getSubmodule())
							.append(
									"</td><td style=\"font-size:16px;font-family:'微软雅黑';\">")
							.append(subj.getUrl().getModule())
							.append(
									"</td><td style=\"font-size:16px;font-family:'微软雅黑';\">")
							.append(formatDate(subj.getPublishDate()))
							.append(
									"</td><td style=\"font-size:16px;font-family:'微软雅黑';\">")
							.append("<a target='_blank' href='").append(
									subj.getSubjUrl()).append("'>").append(
									subj.getSubject()).append("</a></td></tr>");
				} else {
					buf
							.append(
									"<tr><td style=\"font-size:16px;font-family:'微软雅黑';background:#EEECEB\">")
							.append(subj.getUrl().getDepartment())
							.append(
									"</td><td style=\"font-size:16px;font-family:'微软雅黑';background:#EEECEB\">")
							.append(subj.getUrl().getSubmodule())
							.append(
									"</td><td style=\"font-size:16px;font-family:'微软雅黑';background:#EEECEB\">")
							.append(subj.getUrl().getModule())
							.append(
									"</td><td style=\"font-size:16px;font-family:'微软雅黑';background:#EEECEB\">")
							.append(formatDate(subj.getPublishDate()))

							.append(
									"</td><td style=\"font-size:16px;font-family:'微软雅黑';background:#EEECEB\">")
							.append("<a target='_blank' href='").append(
									subj.getSubjUrl()).append("'>").append(
									subj.getSubject()).append("</a></td></tr>");
				}

			}
			buf.append("</table></div>");
		}
		buf.append("</body></html>");
		return buf.toString();
	}

	private void writeExcel(List<Subjects> lst, String fileName)
			throws Exception {
		// 分离subjects，将关键的放到一个list中
		List<Subjects> keyLst = getKeyList(lst);
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}

			WritableWorkbook book = Workbook.createWorkbook(file);

			WritableSheet sheet1 = book.createSheet(DateUtil
					.getCurrentDateTime()
					+ "-关键主题", 0);
			WritableSheet sheet2 = book.createSheet(DateUtil
					.getCurrentDateTime()
					+ "-所有主题", 0);

			sheet1.addCell(new Label(0, 0, "编码"));
			sheet1.addCell(new Label(1, 0, "名称"));
			sheet1.addCell(new Label(2, 0, "网址"));
			sheet1.addCell(new Label(3, 0, "部委"));
			sheet1.addCell(new Label(4, 0, "板块"));
			sheet1.addCell(new Label(5, 0, "子版块"));
			sheet1.addCell(new Label(6, 0, "主题"));
			sheet1.addCell(new Label(7, 0, "发布日期"));
			sheet1.addCell(new Label(8, 0, "主题url"));

			sheet2.addCell(new Label(0, 0, "编码"));
			sheet2.addCell(new Label(1, 0, "名称"));
			sheet2.addCell(new Label(2, 0, "网址"));
			sheet2.addCell(new Label(3, 0, "部委"));
			sheet2.addCell(new Label(4, 0, "板块"));
			sheet2.addCell(new Label(5, 0, "子版块"));
			sheet2.addCell(new Label(6, 0, "主题"));
			sheet2.addCell(new Label(7, 0, "发布日期"));
			sheet2.addCell(new Label(8, 0, "主题url"));

			for (int i = 1; i < keyLst.size(); i++) {// sheet2为关键主题，放到第一个页签
				Subjects s = keyLst.get(i);
				sheet2.addCell(new Label(0, i, s.getUrl().getCode()));
				sheet2.addCell(new Label(1, i, s.getUrl().getName()));
				sheet2.addCell(new Label(2, i, s.getUrl().getUrl()));
				sheet2.addCell(new Label(3, i, s.getUrl().getDepartment()));
				sheet2.addCell(new Label(4, i, s.getUrl().getModule()));
				sheet2.addCell(new Label(5, i, s.getUrl().getSubmodule()));
				sheet2.addCell(new Label(6, i, s.getSubject()));
				sheet2.addCell(new Label(7, i, DateUtil.formatDate(s
						.getPublishDate())));
				sheet2.addCell(new Label(8, i, s.getSubjUrl()));
			}
			for (int i = 1; i < lst.size(); i++) {// sheet1为所有主题，放到第二个页签
				Subjects s = lst.get(i);
				sheet1.addCell(new Label(0, i, s.getUrl().getCode()));
				sheet1.addCell(new Label(1, i, s.getUrl().getName()));
				sheet1.addCell(new Label(2, i, s.getUrl().getUrl()));
				sheet1.addCell(new Label(3, i, s.getUrl().getDepartment()));
				sheet1.addCell(new Label(4, i, s.getUrl().getModule()));
				sheet1.addCell(new Label(5, i, s.getUrl().getSubmodule()));
				sheet1.addCell(new Label(6, i, s.getSubject()));
				sheet1.addCell(new Label(7, i, DateUtil.formatDate(s
						.getPublishDate())));
				sheet1.addCell(new Label(8, i, s.getSubjUrl()));
			}
			book.write();
			book.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(date);
	}

	private List<Subjects> getKeyList(List<Subjects> lst) {
		List<Subjects> keyLst = new ArrayList<Subjects>();
		for (int i = 0; i < lst.size(); i++) {
			Subjects subj = lst.get(i);
			if ("1".equals(subj.getKeyFlag())) {
				keyLst.add(subj);
			}
		}
		return keyLst;
	}

	private List<Subjects> catchPolicyInfo(List<Url> urlList,
			CatchService catchService, List<KeyWords> keyWords)
			throws Exception {
		List<Subjects> result = new ArrayList<Subjects>();
		Iterator<Url> it = urlList.iterator();
		while (it.hasNext()) {
			Url url = it.next();
			result.addAll(catchService.catchPolicy(url, keyWords));
		}
		return result;
	}

}