package com.jg.Model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
	@Id @GeneratedValue
	private int id;
	@ManyToOne(cascade = CascadeType.ALL)
	private User main_author; //int main_author_id;
	@Column(nullable=false)
	private int status;
	@ManyToOne(cascade = CascadeType.ALL)
	private Edition edition;//int edition_id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at;

}