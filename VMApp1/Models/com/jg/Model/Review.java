package com.jg.Model;
import java.sql.*;
import java.util.Date;
import java.util.ArrayList;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Table(name="reviews")
public class Review {
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getReviewer_id() {
		return reviewer_id;
	}
	public void setReviewer_id(int reviewer_id) {
		this.reviewer_id = reviewer_id;
	}
	public int getArticle_id() {
		return article_id;
	}
	public void setArticle_id(int article_id) {
		this.article_id = article_id;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public String getContribution() {
		return contribution;
	}
	public void setContribution(String contribution) {
		this.contribution = contribution;
	}
	public String getCritism() {
		return critism;
	}
	public void setCritism(String critism) {
		this.critism = critism;
	}
	public int getExpertise() {
		return expertise;
	}
	public void setExpertise(int expertise) {
		this.expertise = expertise;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Id @GeneratedValue
	private int id;
	private int reviewer_id;
	private int article_id;
	private int position;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at;
	private String contribution;
	private String critism;
	private int expertise;
	private int status;
}