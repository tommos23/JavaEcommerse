package com.jg.Model;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Table(name="letter_to_editor")
public class LetterToEditor {
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getArticle_id() {
		return article_id;
	}
	public void setArticle_id(int article_id) {
		this.article_id = article_id;
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
	public String getPublish_edition() {
		return publish_edition;
	}
	public void setPublish_edition(String publish_edition) {
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
	private int user_id;
	private int article_id;
	private String text;
	private String edited_text;
	private String reply_text;
	private String status;
	private String publish_edition;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at;
	@Temporal(TemporalType.TIMESTAMP)
	private Date edited_at;
	@Temporal(TemporalType.TIMESTAMP)
	private Date replyed_at;

}
