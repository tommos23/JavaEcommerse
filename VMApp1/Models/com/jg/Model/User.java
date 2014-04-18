package com.jg.Model;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="users")
public class User {
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public Date getCreatedat() {
		return createdat;
	}
	public void setCreatedat(Date createdat) {
		this.createdat = createdat;
	}
	public Date getLastlogin() {
		return lastlogin;
	}
	public void setLastlogin(Date lastlogin) {
		this.lastlogin = lastlogin;
	}
	public String getAffiliations() {
		return affiliations;
	}
	public void setAffiliations(String affiliations) {
		this.affiliations = affiliations;
	}
	
	@Id @GeneratedValue
	private int id;
	
	@Column(name="role_id")
	private int roleid;
	
	@Column(nullable=false, length = 50)
	private String firstname;
	
	@Column(nullable=false, length = 50)
	private String surname;
	
	@Column(nullable=false, columnDefinition = "TEXT")
	private String email;
	
	@Column(nullable=false, length = 50)
	private String password;
	
	private String affiliations;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at")
	private Date createdat;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_login")
	private Date  lastlogin;
}
