package com.jg.Model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Table(name="versions")
public class Version {
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getArticles_id() {
		return articles_id;
	}
	public void setArticles_id(int articles_id) {
		this.articles_id = articles_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAbs() {
		return abs;
	}
	public void setAbs(String abs) {
		this.abs = abs;
	}
	public int getSubject_id() {
		return subject_id;
	}
	public void setSubject_id(int subject_id) {
		this.subject_id = subject_id;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	@Id @GeneratedValue
	private int id;
	private int articles_id;
	@Column(columnDefinition = "TEXT")
	private String title;
	@Column(columnDefinition = "TEXT")
	private String abs;
	private int subject_id;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at;
	
}