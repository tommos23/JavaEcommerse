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
@Table(name="editions")
public class Edition {
	public Edition() {
		super();
	}
	public Edition(String description, Volume vol) {
		this.description = description;
		this.volume = vol;
		this.created_at = new Date();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Volume getVolume() {
		return volume;
	}
	public void setVolume(Volume volume) {
		this.volume = volume;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	@Id @GeneratedValue
	private int id;
	@Column(nullable=false)
	private int status;
	@Column(nullable=false, columnDefinition = "TEXT")
	private String description;
	@ManyToOne(cascade = CascadeType.ALL)
	private Volume volume;//int edition_id;
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at;
}
