package me.kafeitu.demo.activiti.entity.account;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * The persistent class for the ACT_ID_GROUP database table.
 * 
 */
@Entity
@Table(name = "ACT_ID_GROUP")
public class Group implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String type;
	private List<User> actIdUsers;

	public Group() {
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

	@Column(name = "NAME_")
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "TYPE_")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	//bi-directional many-to-many association to User
	@ManyToMany(mappedBy = "actIdGroups")
	public List<User> getActIdUsers() {
		return this.actIdUsers;
	}

	public void setActIdUsers(List<User> actIdUsers) {
		this.actIdUsers = actIdUsers;
	}

}