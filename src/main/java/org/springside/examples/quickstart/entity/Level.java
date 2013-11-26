package org.springside.examples.quickstart.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

//JPA标识
@Entity
@Table(name = "pms_bd_level")
public class Level extends IdEntity {

	private String code;
	private String name;
	private String description;
	private User user;
	private Level parent;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	public Level getParent() {
		return parent;
	}
	
	public void setParent(Level parent) {
		this.parent = parent;
	}

	// JPA 基于USER_ID列的多对一关系定义
	@ManyToOne
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}
	
	
	// JSR303 BeanValidator的校验规则
	public String getDescription() {
		return description;
	}

	@NotBlank
	public String getCode() {
		return code;
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

	public void setDescription(String description) {
		this.description = description;
	}


}
