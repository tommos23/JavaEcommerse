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
@Table(name="messages_to_editor")
public class MessageToEditor {
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
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
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Review getReview() {
		return review;
	}
	public void setReview(Review review) {
		this.review = review;
	}
	
	@Id @GeneratedValue
	private int id;
	@ManyToOne(cascade = CascadeType.ALL)
	private User user;//int user_id;
	@ManyToOne(cascade = CascadeType.ALL)
	private Review review;//int review_id;
	@Column(columnDefinition = "TEXT")
	private String comment;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at;

}