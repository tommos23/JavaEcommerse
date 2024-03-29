package com.jg.Model;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="users")
public class User {
	
	public User(){
		super();
	}
	public User(String firstname,String surname,String email,String password,String affiliations,Date createdate,Date newLastlogin,Date oldLastlogin){
		this.firstname=firstname;
		this.surname=surname;
		this.email=email;
		this.password=password;
		this.affiliations=affiliations;
		this.createdate=createdate;
		this.newLastlogin=newLastlogin;
		this.oldLastlogin = oldLastlogin;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}	
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
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
		return createdate;
	}
	public void setCreatedat(Date createdate) {
		this.createdate = createdate;
	}
	public Date getNewLastlogin() {
		return newLastlogin;
	}
	public void setNewLastlogin(Date newLastlogin) {
		this.newLastlogin = newLastlogin;
	}
	public Date getOldLastlogin() {
		return oldLastlogin;
	}
	public void setOldLastlogin(Date oldLastlogin) {
		this.oldLastlogin = oldLastlogin;
	}
	public String getAffiliations() {
		return affiliations;
	}
	public void setAffiliations(String affiliations) {
		this.affiliations = affiliations;
	}
	
	@Id @GeneratedValue
	private int id;
	
	@ManyToOne(cascade = CascadeType.ALL)
	private Role role;

	@Column(nullable=false, length = 50)
	private String firstname;
	
	@Column(nullable=false, length = 50)
	private String surname;
	
	@Column(nullable=false, unique=true, columnDefinition = "TEXT")
	private String email;
	
	@Column(nullable=false, length = 50)
	private String password;
	
	private String affiliations;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_at")
	private Date createdate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="new_last_login")
	private Date newLastlogin;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="old_last_login")
	private Date oldLastlogin;
}
