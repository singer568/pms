package me.kafeitu.demo.activiti.entity.account;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The persistent class for the ACT_ID_USER database table.
 * 
 */
@Entity
@Table(name = "ACT_ID_USER")
public class ActUser implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String email;
	private String first;
	private String last;
	private String password;

	public ActUser() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID_")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "EMAIL_")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "FIRST_")
	public String getFirst() {
		return this.first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	@Column(name = "LAST_")
	public String getLast() {
		return this.last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	@Column(name = "PWD_")
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}