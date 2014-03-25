package org.springside.examples.quickstart.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

//JPA标识
@Entity
@Table(name = "pms_level")
public class Level extends IdEntity {

	private String code;
	private String name;
	private String description;
	private Level parent;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	public Level getParent() {
		return parent;
	}
	
	public void setParent(Level parent) {
		this.parent = parent;
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
