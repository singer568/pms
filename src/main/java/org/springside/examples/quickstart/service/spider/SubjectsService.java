package org.springside.examples.quickstart.service.spider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springside.examples.quickstart.entity.Email;
import org.springside.examples.quickstart.entity.Subjects;
import org.springside.examples.quickstart.repository.SubjectsDao;
import org.springside.examples.quickstart.service.bd.EmailService;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

//Spring Bean的标识.
@Component
// 默认将类中的所有public函数纳入事务管理.
@Transactional
public class SubjectsService {

	private SubjectsDao subjectsDao;

	private EmailService emailService;

	public List<Subjects> saveAll(List<Subjects> subjs) {
		return (List<Subjects>) subjectsDao.save(subjs);
	}

	public Subjects getSubjects(Long id) {
		return subjectsDao.findOne(id);
	}

	public Subjects saveSubjects(Subjects entity) {
		return subjectsDao.save(entity);
	}

	public void deleteSubjects(Long id) {
		subjectsDao.delete(id);
	}

	public List<Subjects> getAllSubjects() {
		return (List<Subjects>) subjectsDao.findAll();
	}

	public List<Subjects> querySubjectsByParam(Map<String, Object> searchParams) {
		// PageRequest pageRequest = buildPageRequest(1, 50, "catchTime");
		Specification<Subjects> spec = buildSpecification(searchParams);

		return subjectsDao.findAll(spec);
	}

	public List<Subjects> querySubjectsByParam(
			Map<String, Object> searchParams, String sortType) {
//		PageRequest pageRequest = buildPageRequest(1, 50, sortType);
		Specification<Subjects> spec = buildSpecification(searchParams);

		return subjectsDao.findAll(spec);
	}

	public Page<Subjects> getUserSubjects(Map<String, Object> searchParams,
			int pageNumber, int pageSize, String sortType) {
		PageRequest pageRequest = buildPageRequest(pageNumber, pageSize,
				sortType);
		Specification<Subjects> spec = buildSpecification(searchParams);

		return subjectsDao.findAll(spec, pageRequest);
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNumber, int pagzSize,
			String sortType) {
		Sort sort = null;
		if ("auto".equals(sortType)) {
			sort = new Sort(Direction.DESC, "id");
		} else if ("code".equals(sortType)) {
			sort = new Sort(Direction.ASC, "code");
		} else if ("name".equals(sortType)) {
			sort = new Sort(Direction.ASC, "name");
		} else if ("publishDate".equals(sortType)) {
			sort = new Sort(Direction.DESC, "publishDate");
		} else if ("catchTime".equals(sortType)) {
			sort = new Sort(Direction.DESC, "catchTime");
		}

		return new PageRequest(pageNumber - 1, pagzSize, sort);
	}

	/**
	 * 创建动态查询条件组合.
	 */
	private Specification<Subjects> buildSpecification(
			Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<Subjects> spec = DynamicSpecifications.bySearchFilter(
				filters.values(), Subjects.class);
		return spec;
	}

	@Autowired
	public void setSubjectsDao(SubjectsDao subjectsDao) {
		this.subjectsDao = subjectsDao;
	}

	@Autowired
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	public boolean sendMail(String ids, String mail) throws Exception {
		Email fromMail = emailService.getAllEmail().get(0);
		JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
		senderImpl.setHost(fromMail.getHost());
		MimeMessage mailMessage = senderImpl.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage,
				true, "utf-8");
		// 设置收件人，寄件人
		messageHelper.setTo(mail);
		messageHelper.setFrom(fromMail.getEmail());// "tomcat0506@163.com"
		messageHelper.setSubject("政策信息");// "测试邮件中上传附件!！"
		messageHelper.setText(getContent(emailService, ids), true);
		senderImpl.setUsername(fromMail.getUserName());
		senderImpl.setPassword(fromMail.getPwd());
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.timeout", "25000");
		senderImpl.setJavaMailProperties(prop);
		senderImpl.send(mailMessage);

		return true;
	}

	private String getContent(EmailService emailService, String ids) {
		String[] strs = ids.split(",");
		List<Subjects> lst = new ArrayList<Subjects>();
		for (int i = 0; i < strs.length; i++) {
			lst.add(this.getSubjects(Long.parseLong(strs[i])));
		}
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

}
