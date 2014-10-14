package org.springside.examples.quickstart.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

//JPA标识
@Entity
@Table(name = "pms_email")
public class Email implements java.io.Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1151508219513981065L;
	protected Long id;
	private String code;
	private String name;
	private String email;
	private String host;
	private String userName;
	private String pwd;
	// 发生错误后发送到的邮箱地址以#分隔
	private String errEmail;

	private String subject;// 主题
	private String emailContent;// 邮件正文

	private String description;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Email Info{code=").append(code).append(",name=")
				.append(name).append(",email=").append(email).append(",host=")
				.append(host).append(",subject=").append(subject)
				.append(",emailContent=").append(emailContent).append(",user=")
				.append(userName).append(",errEmail=").append(errEmail)
				.append(",description=").append(description);
		return buf.toString();
	}

	@Column(name = "err_email")
	public String getErrEmail() {
		return errEmail;
	}

	public void setErrEmail(String errEmail) {
		this.errEmail = errEmail;
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
		mail.setErrEmail(this.getErrEmail());
		return mail;
	}

	@Column(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "host")
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	@Column(name = "user_name")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "pwd")
	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	@Column(name = "subject")
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(name = "email_content")
	public String getEmailContent() {
		return emailContent;
	}

	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
