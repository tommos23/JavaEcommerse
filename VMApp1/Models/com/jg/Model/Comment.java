package com.jg.Model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="comments")
public class Comment {
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public int getId() {
		return id;
	}
	public int getReview_id() {
		return review_id;
	}
	public int getUser_id() {
		return user_id;
	}
	@Id @GeneratedValue
	private int id;
	private int review_id;
	private int user_id;
	@Column(columnDefinition = "TEXT")
	private String text;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at;

}