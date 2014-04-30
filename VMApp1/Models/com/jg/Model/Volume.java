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
@Table(name="volumes")
public class Volume {
	public Volume(){
		super();
	}
	public Volume(String description){
		this.description = description;
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
	@Column(nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_at;
}
