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
@Table(name="letters_to_editor")
public class LetterToEditor {
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getEdited_text() {
		return edited_text;
	}
	public void setEdited_text(String edited_text) {
		this.edited_text = edited_text;
	}
	public String getReply_text() {
		return reply_text;
	}
	public void setReply_text(String reply_text) {
		this.reply_text = reply_text;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getPublish_edition() {
		return publish_edition;
	}
	public void setPublish_edition(int publish_edition) {
		this.publish_edition = publish_edition;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public Date getEdited_at() {
		return edited_at;
	}
	public void setEdited_at(Date edited_at) {
		this.edited_at = edited_at;
	}
	public Date getReplyed_at() {
		return replyed_at;
	}
	public void setReplyed_at(Date replyed_at) {
		this.replyed_at = replyed_at;
	}
	@Id @GeneratedValue
	private int id;
	@ManyToOne(cascade = CascadeType.ALL)
	private User user;// int user_id;
	@ManyToOne(cascade = CascadeType.ALL)
	private Article article;// int article_id;
	@Column(columnDefinition = "TEXT")
	private String text;
	@Column(columnDefinition = "TEXT")
	private String edited_text;
	@Column(columnDefinition = "TEXT")
	private String reply_text;
	@Column(length = 50)
	private String status;
	private int publish_edition;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at;
	@Temporal(TemporalType.TIMESTAMP)
	private Date edited_at;
	@Temporal(TemporalType.TIMESTAMP)
	private Date replyed_at;

}
