package com.jg.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name="global")
public class Global {

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGoals() {
		return goals;
	}
	public void setGoals(String goals) {
		this.goals = goals;
	}
	public String getSubmission_guidelines() {
		return submission_guidelines;
	}
	public void setSubmission_guidelines(String submission_guidelines) {
		this.submission_guidelines = submission_guidelines;
	}
	@Id @GeneratedValue
	private int id;
	@Column(columnDefinition = "TEXT")
	private String name;
	@Column(columnDefinition = "TEXT")
	private String goals;
	@Column(columnDefinition = "TEXT")
	private String submission_guidelines;

}