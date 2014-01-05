package org.springside.examples.quickstart.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

//JPA标识
@Entity
@Table(name = "pms_email")
public class Email extends IdEntity implements Cloneable {

	private String code;
	private String name;
	private String email;
	private String host;
	private String userName;
	private String pwd;

	private String subject;// 主题
	private String emailContent;// 邮件正文

	private String description;
	private User user;

	// JPA 基于USER_ID列的多对一关系定义
	@ManyToOne
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public Email clone() {
		Email mail = new Email();

		mail.setCode(this.getCode());
		mail.setName(this.getName());
		mail.setEmail(this.getEmail());
		mail.setHost(this.getHost());
		mail.setUserName(this.getUserName());
		mail.setPwd(this.getPwd());
		mail.setSubject(this.getSubject());
		mail.setEmailContent(this.getEmailContent());
		mail.setDescription(this.getDescription());
		mail.setUser(this.getUser());
		return mail;
	}

	// JSR303 BeanValidator的校验规则
	@NotBlank
	public String getCode() {
		return code;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotBlank
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
