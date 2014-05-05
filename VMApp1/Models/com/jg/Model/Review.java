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
@Table(name="reviews")
public class Review {
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getReviewer() {
		return reviewer;
	}
	public void setReviewer(User user) {
		this.reviewer = user;
	}
	public Article getArticle() {
		return article;
	}
	public void setArticle(Article article) {
		this.article = article;
	}
	public Version getVersion() {
		return version;
	}
	public void setVerion(Version version) {
		this.version = version;
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
	@ManyToOne(cascade = CascadeType.MERGE)
	private User reviewer;//int reviewer_id;
	@ManyToOne(cascade = CascadeType.ALL)
	private Article article;//int article_id;
	@ManyToOne(cascade = CascadeType.ALL)
	private Version version;
	private int position;
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at;
	@Column(columnDefinition = "TEXT")
	private String contribution;
	@Column(columnDefinition = "TEXT")	
	private String critism;
	private int expertise;
	private int status;
}