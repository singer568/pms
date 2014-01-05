package org.springside.examples.quickstart.quartz.job;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
		String storeDirectory = "D:/抓取任务/"+"任务一" + "/" + DateUtil.getCurrentDateStr() + "/Excel";
		String mhtDirectory = "D:/抓取任务/"+"任务一" + "/" + DateUtil.getCurrentDateStr() + "/Mht";
		File file = new File(storeDirectory);
		if (!file.exists()){
			file.mkdirs();
		}
		
		File file2 = new File(mhtDirectory);
		if (!file2.exists()){
			file2.mkdirs();
		}
		
		String fileName = storeDirectory + "/"+  System.currentTimeMillis() + ".xlsx";
		File file1 = new File(fileName);
		if (!file1.exists()){
			file1.createNewFile();
		}
	}
	
	
	
	// 增加catchtask参数，明天来了再梳理一下容错信息
	protected void executeInternal(JobExecutionContext jobexecutioncontext)
			throws JobExecutionException {
		JobDetail job = jobexecutioncontext.getJobDetail();

		String excelDirectory = "D:/抓取记录/"+job.getName() + "/" + DateUtil.getCurrentDateStr() + "/Excel";
		File file = new File(excelDirectory);
		if (!file.exists()){
			file.mkdirs();
		}
		JobDataMap map = job.getJobDataMap();
		Object urlList = map.get(SchedulerService.URLLIST);
		Object catchService = map.get(SchedulerService.CATCHSERVICE);
		Object keyWords = map.get(SchedulerService.KEYWORDSLIST);// 查出所有关键词

		String fileName = excelDirectory + "/"+  System.currentTimeMillis() + ".xls";
		
		Email fromEmail = (Email) map.get(SchedulerService.FROMEMAIL);
		List<Subjects> lst = null;
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

			if (to != null && to.size() > 0) {
				String[] tos = new String[to.size()];
				to.toArray(tos);
				sendEmail(fromEmail, tos, fileName);
			}
			((CatchService) catchService).updateSubjects(lst);
		} catch (Exception e) {
			try {
				Email tmp = fromEmail.clone();
				tmp.setSubject("执行任务出错，请及时处理。");

				StringBuffer buf = new StringBuffer();
				buf.append("fileName=").append(fileName).append(",jobName=")
						.append(job.getName()).append(",urlList=").append(
								urlList).append(",keyWords=").append(keyWords);
				buf.append(",fromEmail=").append(tmp.toString()).append(
						",").append("详细错误消息如下：\n");
				buf.append(e.getMessage()).append("\n\n\n堆栈详情如下：\n").append(
						e.getStackTrace());
				tmp.setEmailContent(buf.toString());
				
				sendEmail(tmp, new String[] { "tomcat523@163.com"},
						fileName);
				e.printStackTrace();
				
			} catch (Exception e1) {
				e1.printStackTrace();
				throw new JobExecutionException(e1.getCause());
			}
			throw new JobExecutionException(e.getCause());
		}
	}

	private void sendEmail(Email from, String[] to, String fileName)
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
		messageHelper.setText("<html><head></head><body><h1>"
				+ from.getEmailContent() + "</h1></body></html>", true);

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

			sheet1.addCell(new Label(0, 0, "code"));
			sheet1.addCell(new Label(1, 0, "name"));
			sheet1.addCell(new Label(2, 0, "province"));
			sheet1.addCell(new Label(3, 0, "department"));
			sheet1.addCell(new Label(4, 0, "module"));
			sheet1.addCell(new Label(5, 0, "submodule"));
			sheet1.addCell(new Label(6, 0, "subject"));
			sheet1.addCell(new Label(7, 0, "publishDate"));
			sheet1.addCell(new Label(8, 0, "subjUrl"));

			sheet2.addCell(new Label(0, 0, "code"));
			sheet2.addCell(new Label(1, 0, "name"));
			sheet2.addCell(new Label(2, 0, "province"));
			sheet2.addCell(new Label(3, 0, "department"));
			sheet2.addCell(new Label(4, 0, "module"));
			sheet2.addCell(new Label(5, 0, "submodule"));
			sheet2.addCell(new Label(6, 0, "subject"));
			sheet2.addCell(new Label(7, 0, "publishDate"));
			sheet2.addCell(new Label(8, 0, "subjUrl"));

			for (int i = 1; i < keyLst.size(); i++) {// sheet2为关键主题，放到第一个页签
				Subjects s = keyLst.get(i);
				sheet2.addCell(new Label(0, i, s.getUrl().getCode()));
				sheet2.addCell(new Label(1, i, s.getUrl().getName()));
				sheet2.addCell(new Label(2, i, s.getUrl().getProvince()));
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
				sheet1.addCell(new Label(2, i, s.getUrl().getProvince()));
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