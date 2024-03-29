package com.jg.Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="articles")
public class Article {

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getMainAuthor() {
		return main_author;
	}
	public void setMainAuthor(User user) {
		this.main_author = user;
	}
	public Set<Keyword> getKeywords() {
		return keywords;
	}
	public void setKeywords(Set<Keyword> keywords) {
		this.keywords = keywords;
	}
	public Edition getEdition() {
		return edition;
	}
	public void setEdition(Edition edition) {
		this.edition = edition;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public Version getLatestVersion() {
		return latest_version;
	}
	public void setLatestVersion(Version latest_version) {
		this.latest_version = latest_version;
	}
	public String getContactName() {
		return contact_name;
	}
	public void setContactName(String contactName) {
		this.contact_name = contactName;
	}
	public String getContactEmail() {
		return contact_email;
	}
	public void setContactEmail(String contactEmail) {
		this.contact_email = contactEmail;
	}
	public String getCreatedAtDay(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");      
	    String dateWOTime = sdf.format(created_at);
		return dateWOTime;
	}
	
	@Id @GeneratedValue
	private int id;
	@ManyToOne(cascade = CascadeType.ALL)
	private User main_author; //int main_author_id;
	private String contact_name;
	private String contact_email;
	@Column(nullable=false)
	private int status;
	@ManyToOne(cascade = CascadeType.ALL)
	private Edition edition;//int edition_id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Keyword> keywords = new HashSet<Keyword>();
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Version latest_version;
	
}