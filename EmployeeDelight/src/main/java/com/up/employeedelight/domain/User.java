package com.up.employeedelight.domain;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="users")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private boolean isAdmin;

	private String email;

	private String name;

	private String password;

	private Integer points;

	private String salt;

	@Column(name="userName")
	private String username;

	//uni-directional many-to-one association to UserGroup
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="fk_group")
	private UserGroup userGroup;

	public User() {
	}
	
	public User(String name, String user,String email, String pass, String group) {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean getIsAdmin() {
		return this.isAdmin;
	}

	public void setIsAdmin(boolean admin) {
		this.isAdmin = admin;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getPoints() {
		return this.points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public String getSalt() {
		return this.salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public UserGroup getUserGroup() {
		return this.userGroup;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", isAdmin=" + isAdmin + ", email=" + email
				+ ", name=" + name + ", password=" + password + ", points="
				+ points + ", salt=" + salt + ", username=" + username
				+ ", userGroup=" + userGroup + "]";
	}

}